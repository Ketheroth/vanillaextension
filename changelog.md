# Changelog
All notable changes to this project will be documented in this file.  

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),

## [Unreleased]
## Added
- Sand and Gravel blocks are affected by gravity
- Nether Gold Ore Slab, Stairs, Fence and Wall
- Soul Soil Slab, Stairs, Fence and Wall
- Netherrack blocks can be smelted to obtain nether brick
- Grass Path Fence

## Changed
- Minimum Forge version : 1.16.3-34.0.2
- log trapdoor can be opened with right-click

## [1.16.3-1.0.10]
## Changed
- Port to 1.16.3
- Minimum Forge version : 1.16.3-34.0.1
It should also work in 1.16.2

## [1.16.2-1.0.9]
### Changed
- Port to 1.16.2
- Minimum Forge version : 1.16.2-33.0.41
- (Optional Dependancy) Minimum Patchouli version : 1.16-42

## [1.16.1-1.0.8]
### Added
- Iron Ore Stairs & Wall blasting recipe
- Ore Trapdoor smelting and blasting recipes
- Concrete Powder Stairs, Slabs, Walls, Fences and Trapdoors have gravity and turn into Concrete upon touching water
- Log Trapdoor can be stripped with an axe
- Grass Block Trapdoor spread to dirt blocks
- Coarse Dirt Trapdoor can be tilled with a hoe
- Pumpkin blocks can be sheared to get Carved Pumpkin blocks
- Ore blocks drop xp
- Redstone Ore Slabs, Stairs and Fences light up like Redstone Ore

### Removed
- Ore Slab smelting and blasting recipes

## [1.16.1-1.0.7]
### Added
- Trapdoor variant for most of the vanilla blocks. To not interfere with vanilla recipes, trapdoor recipes from this mod are like this :  
```
###
   
###
```
Where # is the block you want convert in trapdoor.  
Rock based block have stonecutting recipe.
- Stairs, slabs, fences and walls variant for leaves block, vine and grass block use overlay/tintindex textures. The color change depending on the biome.
- Log, Wood, Stem and Hyphae can be stripped to get the stripped version of the block. Work with stairs, slabs, fences and walls.
- Secret advancements
- You can convert Coarse Dirt Blocks to Dirt Blocks with a Hoe.
- VanillaExtension Grass Blocks can spread to all Dirt Blocks. (Minecraft Grass Block can't spread on VanillaExtension Grass Block )

### Changed
- recipes for glass walls don't conflict with glass pane recipe anymore. Recipe for these walls are like the recipes for plank walls

## [1.16.1-1.0.6]
### Added
- Patchouli guide book. (WIP)  
If you have the mod Patchouli in you modpack, a book which describe some recipes/loot\_tables from Vanilla Extension will be possible to craft (book+stick).  
Patchouli is not required to launch my mod. It is only required if you want the guide book.  
- Farmland Slab & Farmland Stairs.  
You can have them by tilling grass/dirt slab/stairs.  
Crops on Farmland Stairs are kinda glitchy, but I can't really do something for it.

### Changed
- better textures for Grass Block Slab, Crimson Nylium Slab, Warped Nylium Slab & Mycelium Slab. The side of the block is now aligned like normal block.
- recipes for plank walls (like oak\_plank\_wall) don't conflict with trapdoors anymore.
The recipes are like :  
```
 # 
###
###
```
Where `#` is a plank.  
And that give 7 walls.


## [1.16.1-1.0.5.2b]
Crash at startup fixed.  
(Yeah I forget to reobf my mod)

## [1.16.1-1.0.5.2]
### Changed
- loot\_tables for stairs, slabs, fences and walls variants for crimson\_nylium, warped\_nylium and gilded\_blackstone now match their origin block loot\_table

## [1.16.1-1.0.5.1]
### Added
- license entry in mods.toml. The mod should work with forge 32.0.90/91/92

### Changed
- Fix malformed advancements for recipes:
  - quartz\_brick\_fence\_from\_quartz\_block\_fence\_stonecutting
  - polished\_basalt\_wall\_from\_basalt\_wall\_stonecutting
  - quartz\_brick\_wall\_from\_quartz\_block\_wall\_stonecutting
- Fix malformed recipes :
  - cracked\_nether\_brick\_fence\_from\_nether\_brick\_fence\_smelting
  - chiseled\_polished\_blackstone\_slab\_from\_blackstone\_slab\_stonecutting
  - chiseled\_polished\_blackstone\_slab\_from\_polished\_blackstone\_slab\_stonecutting
  - cracked\_polished\_blackstone\_brick\_slab\_from\_polished\_blackstone\_brick\_slab\_smelting
  - quartz\_brick\_wall\_from\_quartz\_block\_wall\_stonecutting

## [1.16.1-1.0.5]
### Added
- port to 1.16
- stairs, slabs, fences and walls variant for most of 1.16 new blocks 

## [1.15.2-1.0.4]
#### Added
- Walls
- Chiseled Sandstone Slab & Chiseled Sandstone Stairs
- missing recipes (stonecutting & smelting)
- blasting recipes
- missing advancements to unlock recipes

#### Changed
- better models for fences from blocks with different sides
