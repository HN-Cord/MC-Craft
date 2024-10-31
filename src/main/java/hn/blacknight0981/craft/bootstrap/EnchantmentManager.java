package hn.blacknight0981.craft.bootstrap;

import hn.blacknight0981.craft.bootstrap.enchants.ChainReaction;
import hn.blacknight0981.craft.bootstrap.enchants.Smashing;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;

public class EnchantmentManager implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        new Smashing().bootstrap(context);
        new ChainReaction().bootstrap(context);
    }
}
