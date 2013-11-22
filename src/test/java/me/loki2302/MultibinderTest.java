package me.loki2302;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

public class MultibinderTest {
    @Test
    public void canUseMultibinder() {
        Injector injector = Guice.createInjector(new PluginAModule(), new PluginBModule());
        Framework framework = injector.getInstance(Framework.class);
        assertEquals(2, framework.getPluginCount());
    }
    
    @Singleton
    public static class Framework {
        @Inject
        private Set<Plugin> plugins;
        
        public int getPluginCount() {
            return plugins.size();
        }
    }
    
    public static interface Plugin {
        String getName();
    }
    
    public static class PluginAModule implements Module {
        @Override
        public void configure(Binder binder) {
            Multibinder<Plugin> pluginBinder = Multibinder.newSetBinder(binder, Plugin.class);
            pluginBinder.addBinding().to(PluginA.class);
        }
        
        @Singleton
        public static class PluginA implements Plugin {
            @Inject
            private Framework framework;
            
            @Override
            public String getName() {                
                return "PluginA";
            }            
        }
    }
    
    public static class PluginBModule implements Module {
        @Override
        public void configure(Binder binder) {
            Multibinder<Plugin> pluginBinder = Multibinder.newSetBinder(binder, Plugin.class);
            pluginBinder.addBinding().to(PluginB.class);
        }
        
        @Singleton
        public static class PluginB implements Plugin {
            @Inject
            private Framework framework;
            
            @Override
            public String getName() {                
                return "PluginB";
            }
        }
    }
}
