package com.minecraftabnormals.personality.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class PersonalityConfig {
	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final PersonalityConfig.Client CLIENT;

	static {
		final Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(PersonalityConfig.Client::new);
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();
	}

	public static class Client {
		public final Keybindings keybinds;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("client");
			{
				this.keybinds = new Keybindings(builder);
			}
			builder.pop();
		}
	}

	public static class Keybindings {
		public final ForgeConfigSpec.BooleanValue toggleCrawl;
		public final ForgeConfigSpec.BooleanValue toggleSitting;

		public Keybindings(ForgeConfigSpec.Builder builder) {
			builder.comment("Options for Personality keybindings.").push("keybindings");
			{
				this.toggleCrawl = builder.comment("If true, crawling will be toggled on or off instead of the keybinding being held down. (Default: false)").define("toggleCrawl", false);
				this.toggleSitting = builder.comment("If true, sitting will be toggled on or off instead of the keybinding being held down. (Default: true)").define("toggleSitting", true);
			}
			builder.pop();
		}
	}
}