package com.goodbird.salamanderlib.asm;

import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;

import java.util.ArrayList;

public interface IAdvController {

    ArrayList<BedrockEmitter> getEmitters();

    long getLastTick();
    void setLastTick(long tick);
}
