# lithiumforge Configuration File Summary
The configuration file makes use of the [Java properties format](https://docs.oracle.com/cd/E23095_01/Platform.93/ATGProgGuide/html/s0204propertiesfileformat01.html). If the configuration file does not exist during game start-up, a blank file with a comment will be created.

The configuration file defines *overrides* for the available options, and as such, a blank file is perfectly normal! It simply means that you'd like to use all the default values.

Each category below includes a list of options which can be changed by the user. Due to the nature of the mod, configuration options require a game restart to take effect.

### Editing the configuration file

Before editing the configuration file, take a backup of your minecraft worlds!
All configuration options are simple key-value pairs. In other words, you first specify the option's name, followed by the desired value, like so:

```properties
mixin.ai.pathing=false
mixin.gen.biome_noise_cache=false
```

# Configuration options
### `mixin.chunk`
(default: `true`)  
Various world chunk optimizations  
  
### `mixin.chunk.entity_class_groups`
(default: `true`)  
Allow grouping entity classes for faster entity access, e.g. boats and shulkers  
  
### `mixin.chunk.no_locking`
(default: `true`)  
Remove debug checks in block access code  
  
### `mixin.chunk.no_validation`
(default: `true`)  
Skip bounds validation when accessing blocks  
  
### `mixin.chunk.palette`
(default: `true`)  
Replaces the vanilla hash palette with an optimized variant  
  
### `mixin.chunk.serialization`
(default: `true`)  
Optimizes chunk palette compaction when serializing chunks  
  
### `mixin.entity`
(default: `true`)  
Various entity optimizations  
  
### `mixin.entity.collisions`
(default: `true`)  
Various entity collision optimizations  
  
### `mixin.entity.collisions.intersection`
(default: `true`)  
Uses faster block access for block collisions and delayed entity access with grouped boat/shulker for entity collisions when available  
Requirements:
- `mixin.util.block_tracking=true`
- `mixin.util.chunk_access=true`  
  
### `mixin.entity.collisions.movement`
(default: `true`)  
Entity movement uses optimized block access and optimized and delayed entity access  
Requirements:
- `mixin.util.chunk_access=true`  
  
### `mixin.entity.collisions.unpushable_cramming`
(default: `true`)  
In chunks with many mobs in ladders a separate list of pushable entities for cramming tests is used  
Requirements:
- `mixin.chunk.entity_class_groups=true`  
  
### `mixin.entity.fire`
(default: `true`)  
Optimize checking if entity is in fire or lava  
  
### `mixin.util`
(default: `true`)  
Various utilities for other mixins  
  
### `mixin.util.block_tracking`
(default: `true`)  
Chunk sections count certain blocks inside them and provide a method to quickly check whether a chunk contains any of these blocks  
  
### `mixin.util.block_tracking.block_listening`
(default: `true`)  
Chunk sections can notify registered listeners about certain blocks being placed or broken  
  
### `mixin.util.chunk_access`
(default: `true`)  
Access chunks of worlds, chunk caches and chunk regions directly.  
  
