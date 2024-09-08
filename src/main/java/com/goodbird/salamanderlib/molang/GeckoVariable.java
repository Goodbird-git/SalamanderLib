package com.goodbird.salamanderlib.molang;


import software.bernie.geckolib3.core.molang.LazyVariable;
import software.bernie.shadowed.eliotlash.mclib.math.Variable;

public class GeckoVariable extends Variable {
    public GeckoVariable(String name, double value) {
        super(name, value);
    }

    public double get() {
        if(this!= MolangRegistrar.getParser().getVariable(getName())){
            return MolangRegistrar.getParser().getVariable(getName()).get();
        }
        return super.get();
    }
}
