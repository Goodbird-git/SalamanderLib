package com.goodbird.salamanderlib.molang.expressions;


import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.goodbird.salamanderlib.molang.MolangParser;
import software.bernie.shadowed.eliotlash.mclib.math.Constant;
import software.bernie.shadowed.eliotlash.mclib.math.IValue;

public class MolangValue extends MolangExpression {
    public IValue value;
    public boolean returns;

    public MolangValue(MolangParser context, IValue value) {
        super(context);
        this.value = value;
    }

    public MolangExpression addReturn() {
        this.returns = true;
        return this;
    }

    public double get() {
        return this.value.get();
    }

    public String toString() {
        return (this.returns ? "return " : "") + this.value.toString();
    }

    public JsonElement toJson() {
        return (JsonElement)(this.value instanceof Constant ? new JsonPrimitive(this.value.get()) : super.toJson());
    }
}
