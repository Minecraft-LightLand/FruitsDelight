package dev.xkmc.fruitsdelight.content.effects;

import dev.xkmc.l2library.base.effects.api.ClientRenderEffect;
import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.FirstPlayerRenderEffect;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

public abstract class RangeRenderEffect extends RangeSearchEffect implements ClientRenderEffect, FirstPlayerRenderEffect {

	protected RangeRenderEffect(MobEffectCategory pCategory, int pColor) {
		super(pCategory, pColor);
	}

	@Override
	public void render(LivingEntity entity, int lv, Consumer<DelayedEntityRender> consumer) {
		if (entity == Proxy.getClientPlayer()) return;
		renderEffect(lv, entity);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void onClientLevelRender(AbstractClientPlayer player, MobEffectInstance ins) {
		renderEffect(ins.getAmplifier(), player);
	}

	private void renderEffect(int lv, Entity entity) {
		if (Minecraft.getInstance().isPaused()) return;
		int r = getRange();
		int count = getParticleCount(lv);
		for (int i = 0; i < count; i++) {
			addParticle(entity.level(), entity.position().add(0, entity.getEyeHeight() / 2, 0), r);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private void addParticle(Level w, Vec3 vec, int r) {
		float tpi = (float) (Math.PI * 2);
		Vec3 v0 = new Vec3(0, r, 0);
		v0 = v0.xRot(tpi / 4).yRot(w.getRandom().nextFloat() * tpi);
		w.addAlwaysVisibleParticle(getParticle(),
				vec.x + v0.x,
				vec.y + v0.y,
				vec.z + v0.z, 0, 0, 0);
	}

	protected int getParticleCount(int lv) {
		return 5;
	}

	protected abstract ParticleOptions getParticle();

}
