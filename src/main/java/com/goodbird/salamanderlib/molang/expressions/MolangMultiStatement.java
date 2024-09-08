//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.goodbird.salamanderlib.molang.expressions;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import com.goodbird.salamanderlib.molang.MolangParser;
import software.bernie.shadowed.eliotlash.mclib.math.Variable;

public class MolangMultiStatement extends MolangExpression {
    public final List<MolangExpression> expressions = new ObjectArrayList();
    public final Map<String, Variable> locals = new Object2ObjectOpenHashMap();

    public MolangMultiStatement(MolangParser context) {
        super(context);
    }

    public double get() {
        double value = 0.0;

        MolangExpression expression;
        for(Iterator var3 = this.expressions.iterator(); var3.hasNext(); value = expression.get()) {
            expression = (MolangExpression)var3.next();
        }

        return value;
    }

    public String toString() {
        StringJoiner builder = new StringJoiner("; ");
        Iterator var2 = this.expressions.iterator();

        while(var2.hasNext()) {
            MolangExpression expression = (MolangExpression)var2.next();
            builder.add(expression.toString());
            if (expression instanceof MolangValue && ((MolangValue)expression).returns) {
                break;
            }
        }

        return builder.toString();
    }
}
