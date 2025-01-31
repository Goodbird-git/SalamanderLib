//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.goodbird.salamanderlib.molang;

import com.goodbird.salamanderlib.molang.expressions.MolangAssignment;
import com.goodbird.salamanderlib.molang.expressions.MolangExpression;
import com.goodbird.salamanderlib.molang.expressions.MolangValue;
import com.goodbird.salamanderlib.molang.functions.AcosDegrees;
import com.goodbird.salamanderlib.molang.functions.AsinDegrees;
import com.goodbird.salamanderlib.molang.functions.Atan2Degrees;
import com.goodbird.salamanderlib.molang.functions.AtanDegrees;
import com.google.gson.JsonElement;
import software.bernie.geckolib3.core.molang.LazyVariable;
import software.bernie.geckolib3.core.molang.MolangException;
import com.goodbird.salamanderlib.molang.expressions.MolangMultiStatement;
import software.bernie.geckolib3.core.molang.functions.CosDegrees;
import software.bernie.geckolib3.core.molang.functions.SinDegrees;
import software.bernie.shadowed.eliotlash.mclib.math.Constant;
import software.bernie.shadowed.eliotlash.mclib.math.IValue;
import software.bernie.shadowed.eliotlash.mclib.math.Variable;

import java.util.ArrayList;
import java.util.List;

public class AdvMolangParser extends MolangParser {
    public static final MolangExpression ZERO = new MolangValue(null, new Constant(0));
    public static final MolangExpression ONE = new MolangValue(null, new Constant(1));
    public static final String RETURN = "return ";

    private MolangMultiStatement currentStatement;
    private boolean registerAsGlobals;

    public AdvMolangParser()
    {
        /* Replace radian based sin and cos with degreebased */
        this.functions.put("cos", CosDegrees.class);
        this.functions.put("sin", SinDegrees.class);
        this.functions.put("acos", AcosDegrees.class);
        this.functions.put("asin", AsinDegrees.class);
        this.functions.put("atan", AtanDegrees.class);
        this.functions.put("atan2", Atan2Degrees.class);

        this.remap("cos", "math.cos");
        this.remap("sin", "math.sin");

        /* New functions in 1.16 */
        this.remap("acos", "math.acos");
        this.remap("asin", "math.asin");
        this.remap("atan", "math.atan");
        this.remap("atan2", "math.atan2");
//        this.remap("randomi", "math.random_integer");
//        this.remap("roll", "math.die_roll");
//        this.remap("rolli", "math.die_roll_integer");
//        this.remap("hermite", "math.hermite_blend");

//        /* Remap variables as well */
//        this.remapVar("PI", "math.pi");
    }


    /**
     * Remap variable names
     */
    public void remapVar(String old, String newName)
    {
        this.variables.put(newName, this.variables.remove(old));
    }

    /**
     * Interactively return a new variable
     */
    @Override
    public Variable getVariable(String name)
    {
        Variable variable = this.currentStatement == null ? null : this.currentStatement.locals.get(name);

        if (variable == null)
        {
            variable = super.getVariable(name);
        }

        if (variable == null)
        {
            variable = new LazyVariable(name, 0);

            this.register(variable);
        }

        return variable;
    }

    public MolangExpression parseGlobalJson(JsonElement element) throws MolangException
    {
        this.registerAsGlobals = true;

        MolangExpression expression = parseJson(element);

        this.registerAsGlobals = false;

        return expression;
    }

    /**
     * Parse a molang expression
     */
    public MolangExpression parseExpression(String expression) throws MolangException
    {
        List<String> lines = new ArrayList<String>();

        for (String split : expression.toLowerCase().trim().split(";"))
        {
            if (!split.trim().isEmpty())
            {
                lines.add(split);
            }
        }

        if (lines.size() == 0)
        {
            throw new MolangException("Molang expression cannot be blank!");
        }

        MolangMultiStatement result = new MolangMultiStatement(this);

        this.currentStatement = result;

        try
        {
            for (String line : lines)
            {
                result.expressions.add(this.parseOneLine(line));
            }
        }
        catch (Exception e)
        {
            this.currentStatement = null;

            throw e;
        }

        this.currentStatement = null;

        return result;
    }

    /**
     * Parse a single Molang statement
     */
    protected MolangExpression parseOneLine(String expression) throws MolangException
    {
        expression = expression.trim();

        if (expression.startsWith(RETURN))
        {
            try
            {
                return new MolangValue(this, this.parse(expression.substring(RETURN.length()))).addReturn();
            }
            catch (Exception e)
            {
                throw new MolangException("Couldn't parse return '" + expression + "' expression!");
            }
        }

        try
        {
            List<Object> symbols = this.breakdownChars(this.breakdown(expression));

            /* Assignment it is */
            if (symbols.size() >= 3 && symbols.get(0) instanceof String && this.isVariable(symbols.get(0)) && symbols.get(1).equals("="))
            {
                String name = (String) symbols.get(0);
                symbols = symbols.subList(2, symbols.size());

                Variable variable = null;

                if (!this.registerAsGlobals && !this.variables.containsKey(name) && !this.currentStatement.locals.containsKey(name))
                {
                    variable = new Variable(name, 0);
                    this.currentStatement.locals.put(name, variable);
                }
                else
                {
                    variable = this.getVariable(name);
                }

                return new MolangAssignment(this, variable, this.parseSymbolsMolang(symbols));
            }

            return new MolangValue(this, this.parseSymbolsMolang(symbols));
        }
        catch (Exception e)
        {
            throw new MolangException("Couldn't parse '" + expression + "' expression!");
        }
    }

    /**
     * Wrapper around {@link #parseSymbols(List)} to throw {@link MolangException}
     */
    private IValue parseSymbolsMolang(List<Object> symbols) throws MolangException
    {
        try
        {
            return this.parseSymbols(symbols);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            throw new MolangException("Couldn't parse an expression!");
        }
    }
}