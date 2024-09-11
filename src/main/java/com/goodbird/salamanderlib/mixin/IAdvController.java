package com.goodbird.salamanderlib.mixin;

import com.goodbird.salamanderlib.particles.emitter.BedrockEmitter;

import java.util.ArrayList;
import java.util.List;

public interface IAdvController {

    List<BedrockEmitter> getEmitters();

    long getLastTick();
    void setLastTick(long tick);
}
