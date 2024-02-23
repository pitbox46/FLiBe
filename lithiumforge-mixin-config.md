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
### `mixin.ai`
(default: `true`)  
Mob AI optimizations  
  
### `mixin.ai.nearby_entity_tracking`
(default: `false`)  
Event-based system for tracking nearby entities.
  
Requirements:
- `mixin.util.entity_section_position=true`  
  
### `mixin.ai.nearby_entity_tracking.goals`
(default: `true`)  
A number of AI goals which query for nearby entities in the world every tick will use the event-based
system for tracking nearby entities. In other words, instead of entities constantly polling to see if
other entities are nearby, they will instead be notified only occasionally when such an entity enters
their range.
  
  
### `mixin.ai.pathing`
(default: `true`)  
A faster code path is used for determining what kind of path-finding node type is associated with a
given block. Additionally, a faster chunk cache will be used for accessing blocks while evaluating
paths.
  
Requirements:
- `mixin.util.chunk_access=true`  
  
### `mixin.ai.poi`
(default: `true`)  
Implements a faster POI search  
  
### `mixin.ai.poi.fast_portals`
(default: `true`)  
Portal search uses the faster POI search and optimized loaded state caching  
  
### `mixin.ai.raid`
(default: `true`)  
Avoids unnecessary raid bar updates and optimizes expensive leader banner operations  
  
### `mixin.ai.sensor.secondary_poi`
(default: `true`)  
Avoid unnecessary secondary POI searches of non-farmer villagers  
  
### `mixin.ai.task`
(default: `true`)  
Various AI task optimizations  
  
### `mixin.ai.task.launch`
(default: `true`)  
Keep track of running and runnable tasks to speed up task launching checks  
  
### `mixin.ai.task.memory_change_counting`
(default: `true`)  
Keep track of AI memory changes to skip checking AI task memory prerequisites  
  
### `mixin.ai.task.replace_streams`
(default: `true`)  
Replace Stream code of AI tasks with more traditional iteration.  
  
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
  
