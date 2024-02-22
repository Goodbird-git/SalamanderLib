package mchorse.blockbuster.client.particles.components.rate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mchorse.blockbuster.client.particles.components.BedrockComponentBase;
import mchorse.blockbuster.client.particles.components.IComponentEmitterUpdate;
import mchorse.blockbuster.client.particles.emitter.BedrockEmitter;
import software.bernie.shadowed.eliotlash.mclib.math.Constant;
import software.bernie.shadowed.eliotlash.mclib.math.Operation;
import software.bernie.shadowed.eliotlash.molang.MolangException;
import software.bernie.shadowed.eliotlash.molang.MolangParser;
import software.bernie.shadowed.eliotlash.molang.expressions.MolangExpression;
import software.bernie.shadowed.eliotlash.molang.expressions.MolangValue;

public class BedrockComponentRateInstant extends BedrockComponentRate implements IComponentEmitterUpdate
{
    public static final MolangExpression DEFAULT_PARTICLES = new MolangValue(null, new Constant(10));

    public BedrockComponentRateInstant()
    {
        this.particles = DEFAULT_PARTICLES;
    }

    public BedrockComponentBase fromJson(JsonElement elem, MolangParser parser) throws MolangException
    {
        if (!elem.isJsonObject()) return super.fromJson(elem, parser);

        JsonObject element = elem.getAsJsonObject();

        if (element.has("num_particles"))
        {
            this.particles = parser.parseJson(element.get("num_particles"));
        }

        return super.fromJson(element, parser);
    }

    @Override
    public JsonElement toJson()
    {
        JsonObject object = new JsonObject();

        if (!MolangExpression.isConstant(this.particles, 10))
        {
            object.add("num_particles", this.particles.toJson());
        }

        return object;
    }

    @Override
    public void update(BedrockEmitter emitter)
    {
        double age = emitter.getAge();

        if (emitter.playing && Operation.equals(age, 0))
        {
            emitter.setEmitterVariables(0);

            for (int i = 0, c = (int) this.particles.get(); i < c; i ++)
            {
                emitter.spawnParticle();
            }
        }
    }
}