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
		public final Keybinds keybinds;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("client");
			{
				this.keybinds = new Keybinds(builder);
			}
			builder.pop();
		}
	}

	public static class Keybinds {
		public final ForgeConfigSpec.BooleanValue toggleCrawl;

		public Keybinds(ForgeConfigSpec.Builder builder) {
			builder.comment("Options for Personality keybinds.").push("keybinds");
			{
				this.toggleCrawl = builder.comment("If true, crawl will be toggled when the keybind is pressed instead of holding the key.").define("toggleCrawl", false);
			}
			builder.pop();
		}
	}
}
