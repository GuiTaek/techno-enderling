# general mod concept: Techno Enderling
# becoming a Techno Enderling
- [ ] you have to eat a special food that can be buyed from the wandering trader
and be crafted from a warped fungus and an ender pearl
- [ ] when drunken in a soup or a potion grants an effect, nausea and weakness
- [ ] when dying due to an enderman whilst under this effect, you respawn (even in hardcore)
as an Techno Enderling on world spawn without a set spawn
- [ ] cheating hardcore by eating the food that make you the creature you already are
doesn't work
- [ ] when you are a techno enderling, eating the food "of your race" grants you one enderpearl charge with high probability
- [ ] there is a similar food crafted from a brown mushroom that can transform you back into
a human
- [ ] whilst under the effect, every enderman seeing you is angry at you
- [ ] you can decide at the start of the game to be a Techno Enderling and the food isn't craftable or both not (gamerule)
# general portal infos
## take entities, players, villagers with you
- [x] you can enter the portals also while riding an entity like a boat, a minecart (todo), a horse (variant) or (in later versions) a camel
- [x] everything you rode and that rode with you will follow you
- [ ] tickets except for one case propagate through enderworld portals one chunk
- [ ]´the one case being from an enderworld portal in enderworld to another enderworld portal in the enderworld
- [ ] probably even needed for prevention of infinite recursion
- [ ] tickets from players propagate through pocket portals to their whole respective pocket dimension (being limited in size)
# enderworld
- [x] by design only useful for Techno Enderlings
- [x] is a new dimension, the origin of the Enderlings
- [x] extremly rarely, non-renewable and persistant endermans spawn there, but there are structures that make them spawn frequently and other mobs spawn
- [x] one-way-portals generate (else the overworld would get cluttered with
these portals when you're long enough in a world)
- [x] pocket-portals generate
- [x] some endertrees generate (so you can make tools to break out of a stone-layer
that you spawned in when entering a one-way-portal)
- [x] to make tools of stone you have to go to the overworld by design
- [ ] spawning in the enderworld, as it is less dense than the overworld, you are more spread out to spawn (because 21*16 = 336: 336x336)

# pocket dimension
- [x] unique to every player
- [x] contains in the middle a pocket-portal
- [x] no mobs spawn naturally, you have to move every mob you want with a boat

# non-buildable structures in the enderworld
## one-way-portals
- [x] teleports you to the surface (the trees are still needed to kill the endermans)
## pocket-portal
- [ ] only a Techno Enderling can enter
- [x] can't be destroyed in survival (maybe because it's out of bedrock)
- [x] very big
- [x] can be accessed through boats and minecarts
- [ ] a Techno Enderling can repair a broken pocket portal with dragon immune blocks (because you can in fact destroy bedrock, even if I'm not a fan of this mechanic, I'm not going to fix it)
- [x] are meant to be a little bit difficult to find (a few minutes)
- [x] in an enderworld foggy forest it shall be easy to find one, even with the fog
- [x] this helps to make it possible for potato pcs possibly needing a render distance of 5
- [x] I'm planning to test these last two points in survival with a render distance of 5
- [x] destroys every block inside its bounding box on entering so you better don't build your storage inside the portal
- [x] from a destroyed inventory, only some items survive
### pocket dimension
- [x] teleports the player to the last pocket portal the player used
### enderworld
- [x] teleports the player to its own pocket dimension
- [ ] every player has its own pocket dimension
# buildable structues
## portal
- [x] every player can use it
- [x] spawn naturally and activated in the overworld but nowhere else
- [x] when spawned naturally in the overworld, they automatically link to a portal on the surface of the enderworld
  - [x] it is meant to be easy to find one
- [x] is build out of a glowstone block and two gold Blocks above, however not
the raw blocks but blocks that have been made useless with ender essence. The glowstone
blocks drops raw glowstone dust
- [x] right-clicking and holding teleports you after some time (todo: pretty animation like e.g. particles and FOV)
- [x] breaking one block deactivates the portal
### enderworld
- [x] lead to the overworld
- [x] every block in the overworld corresponds to a chunk in the enderworld
- [x] the y coordinate is exact on building
- [x] consumes while activating 2 gold blocks and 1 glowstone block
- [x] teleports to the nearest y-wise portal
- [x] will break 3x3x3 blocks on activation on the other side (not when going through) and, if necessary, a plattform of endstone (that must be provided)
### overworld
- [x] lead to the enderworld
- [x] the x,z coordinate is random in the chunk, the y coordinate is exact
- [x] consumes while activating 2 gold blocks and 1 glowstone block
- [x] teleports to the nearest y-wise portal
- [x] will break 3x3x3 blocks on activation on the other side (not when going through) and, if necessary, a plattform of different stone types (that must be provided)

### nether
- [ ] because of the nether being so intertwined, you can use the portal here to teleport to known other portals in the nether
- [ ] every Player need an enderpearl to teleport that is consumed (techno enderlings can also consume an ender-pearl-charge)

### end
- [ ] you spawn on your spawn, just as if you've gone through the end-portal
- [ ] if your spawn in in the end, then you spawn in the enderworld at world spawn

## pocket dimension
- [x] has no use here

## enderling anchor
- [ ] only Techno Enderlings can use them
- [ ] if a non-Techno-Enderling uses them, they explode
- [ ] on every other dimension than the pocket dimension, you reset your spawn point there
- [ ] works on every dimension, including the nether and the end
- [ ] on the pocket dimension it teleports you to your spawn point on holding right click (so it can't be abused as easy block breaker)
- [ ] build out of an amethyst block and 8 copper blocks surrounding
## reactor
- [ ] they only work in the enderworld
- [ ] need a core and a mantle
- [ ] the core is just one block of crying obsidian
- [ ] the mantle covers every side of the crying obsidian
- [ ] the reactor needs a sacrifice on activation
### overworld reactor
- [ ] mantle: stone
- [ ] sacrifice:
    - 16 rotten flesh
    - 16 bones
    - 16 strings
    - 16 gunpowder
### weak ender reactor
- [ ] mantle: magenta or purple glass
- [ ] sacrifice: 1 ender pearl
- [ ] spawns an enderman every some minutes
### strong ender reactor
- [ ] mantle: purpur block
- [ ] sacrifice: 16 ender pearls
- [ ] spawns endermans as in the end
### nether reactor
- [ ] mantle: nether rack
- [ ] sacrifice: 16 gold ingots
- [ ] spawn creatures like in the nether waste biome
- [ ] piglins are safe here and piglins starting to convert pause their conversion
- [ ] possible advancement: breed a hoglin in the enderworld
  - [ ] I will test if it's possible and if not, a nether reactor or nether fortress reactor
in the overworld will slow the conversion down
### nether fortress reactor
- [ ] mantle: weak nether bricks
- [ ] sacrifice: 16 blaze rods
- [ ] spawns nether fortress mobs, but only on nether bricks
- [ ] wither roses can be placed on nether bricks in the whole enderworld
- [ ] piglins are safe here and piglins starting to convert pause their conversion
## Enderling Ward
- [ ] there is a weak, medium and strong ward
- [ ] the weak needs an iron block as core
- [ ] the medium needs a diamond block as core
- [ ] the strong needs a beacon block as core
- [ ] the strong should be enough to fit some farms and a villager trading hall
- [ ] without a ward, the dimension should be enough to barely fit 4 single chest blocks outside the portal
- [ ] the core is surrounded by end Stone bricks
- [ ] the lower side has two end stone bricks
### pocket dimension
- [ ] gives you more space in your pocket dimension
- [ ] only works when placed on top of the portal in the fitting position
### enderworld, end
- [ ] repells water and lava
### overworld
- [ ] repells water
### nether
- [ ] repells lava
## soul block
- [ ] made out of one soul sand block
- [ ] makes mobs spawn as if there were a player
- [ ] doesn't increase the mob cap

# things to consider when building this mod
- [ ] don't let the player abuse the pocket dimension teleportation
to "remember" one location (shouldn't be possible except for when another
mod already give the ability to remember a position, but that should be fine)
- [ ] the endermans has to spawn very rarely in the enderworld so
the eating nerf is actually a huge nerf -> you are meant to build a weak ender reactor soon and when away from your base
- [ ] taking with you villagers, players and animals is wanted

# abilities
- [ ] can build structures and activate them using a long use hold, they use up ender essence
and maybe some items as sacrifice
- [ ] spawns naturally in the enderworld
- [ ] can absorb ender pearls by shift right click-holding them
- [ ] can absorb up to 4 enderpearls
- [ ] when the ender essence runs out, it automatically refreshes from the consumed enderpearls
- [ ] can't eat
- [ ] saturation has no effect, also hunger
- [ ] ender essence slowly (!!) replenish saturation
- [ ] because you didn't lived in the end, only in the enderworld, you don't have the usual enderman properties like being damaged from water, being 3 blocks tall or being able to teleport without aid
- [ ] your skin is a little darker
- [ ] you can sleep, but this won't reset your spawn
- [ ] you can enter pocket portals
- [ ] endermans don't get aggressive when you look at them (that's actually a nerf because they don't teleport to you when you look at them)
- [ ] you don't take ender pearl damage
