package com.goodbird.salamanderlib.molang.expressions;


import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.goodbird.salamanderlib.molang.MolangParser;
import software.bernie.shadowed.eliotlash.mclib.math.Constant;
import software.bernie.shadowed.eliotlash.mclib.math.IValue;
import software.bernie.shadowed.eliotlash.mclib.math.Operation;

public abstract class MolangExpression implements IValue {
    public MolangParser context;

    public static boolean isZero(MolangExpression expression) {
        return isConstant(expression, 0.0);
    }

    public static boolean isOne(MolangExpression expression) {
        return isConstant(expression, 1.0);
    }

    public static boolean isConstant(MolangExpression expression, double x) {
        if (!(expression instanceof MolangValue)) {
            return false;
        } else {
            MolangValue value = (MolangValue)expression;
            return value.value instanceof Constant && Operation.equals(value.value.get(), x);
        }
    }

    public static boolean isExpressionConstant(MolangExpression expression) {
        if (expression instanceof MolangValue) {
            MolangValue value = (MolangValue)expression;
            return value.value instanceof Constant;
        } else {
            return false;
        }
    }

    public MolangExpression(MolangParser context) {
        this.context = context;
    }

    public JsonElement toJson() {
        return new JsonPrimitive(this.toString());
    }
}
