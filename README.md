### Description

A custom death system that adds ghosts and also includes custom death messages  

### Commands

- /morto - Kills a player  
- /resuscita - Attempt to revive a player (Must be inside the specified region)  
- /trovaanima [player] - Tracks the player given, or selects a random player if none specified  
- /death - Reloads the plugin  
  

### Permission

- death.nether - Allows player to use nether portals  
- death.death - Makes the player transparent aswell as removing hunger  
- death.invincibility - Makes the player invincible  
- death.kill - Allows player to use /morto  
- death.compass - Allows player to use /trovaanima  
- death.revival - Allows player to use /resuscita  
- death.reload - Allows player to reload the plugin  
- death.bypass - Allows player bypass the death properties  
  

### Config
```yaml
#Where players respawn after dieing
respawnWorld: world_nether

#Delay in seconds after which messages will be sent (on respawn)
respawnMessageDelay: 5

#What group players get put in when they die
deathGroup: "Morto"

#revive command if revival group remove doesn't work
reviveCommand: "pex user %player% group remove morto"

#How long a player waits to be revive (in seconds)
reviveDelay: 30

#What region should the player be in when they are being revived
reviveRegion: "Lotus"

#Resurrect cooldown in minutes
resurrectCooldown: 60

#Disable in the following worlds
worldBlacklist:
- "one"
- "two"

#Compass item
compass:
  display: "&cTracking %player%"
  lore:
  - "&6World: %world%"
  - "&6X: %x%"
  - "&6Y: %y%"
  - "&6Z: %z%"
  #Max/Min the coordinates can be wrong by
  wrong-x: 50
  wrong-z: 50

#List of death messages
deathMessages:
- "%target_name% was slain by %killer_name% in world named %world%"
- "%target_name% died"
- "%killer_name% killed %target_name%"
- "%target_name% was killed in world named %world%"

#Particles that spawn at revival location
particles:
  1:
    #List of particles to show
    particle:
    - DRAGON_BREATH
    - FLAME
    #Delay before the particles show - the delay is based from the start, not incrementing from the last delay (in seconds)
    delay: 1
  2:
    particle:
    - HEART
    - NOTE
    delay: 6
  3:
    particle:
    - REDSTONE
    - PORTAL
    - TOTEM
    delay: 10

#Messages shown at revival location
messages:
  1:
    message: "&1Message one"
    delay: 10
    range: 50
  2:
    message: "&2Message two only visible inside 10 blocks"
    delay: 11
    range: 10
  3:
    message: "&3Message three"
    delay: 20
    range: 50
  4:
    message: "&dDisabled message"
    delay: 15
    range: 0

deathTypes:
  BLOCK_EXPLOSION: "an explosion"
  DROWNING: "drowning"
  FALL: "falling"
  FIRE: "fire"
  LAVA: "lava"
  SUFFOCATION: "suffocation"
  STARVATION: "starvation"
  POISON: "poison"
  PROJECTILE: "projectile"
  SUICIDE: "suicide"
  DEFAULT: "a mob"
  ```

