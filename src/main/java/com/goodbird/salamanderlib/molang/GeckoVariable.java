package com.goodbird.salamanderlib.molang;


import software.bernie.geckolib3.core.molang.LazyVariable;

public class GeckoVariable extends LazyVariable {
    public GeckoVariable(String name, double value) {
        super(name, value);
    }

    public double get() {
        if(this!= MolangRegistrar.getParser().getVariable(getName(), null)){
            return MolangRegistrar.getParser().getVariable(getName(), null).get();
        }
        return super.get();
    }
}
