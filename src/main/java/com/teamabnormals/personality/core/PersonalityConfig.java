package com.teamabnormals.personality.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class PersonalityConfig {

	public static class Client {
		public final ConfigValue<Boolean> climbingAnimation;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("animation");
			this.climbingAnimation = builder.comment("If there should be a special animation for climbing ladders").define("Climbing animation", true);
			builder.pop();
		}
	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	static {
		Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = clientSpecPair.getRight();
		CLIENT = clientSpecPair.getLeft();
	}
}
