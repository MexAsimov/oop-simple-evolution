# oop-simple-evolution
[AGH-LAB] Simple project checking basic skills in Java OOP.

## Layout

![image](https://user-images.githubusercontent.com/58474974/143791899-ee571bda-697c-4819-b7f8-c8115b604492.png)

## Description

Two seperated worlds created to show process of simple evolution. Animals (blue squares) are randomly moving over the map and eating Plants (green squares). If animal doesn't gather enough energy from plants his energy runs to zero and animal die. If more than one animal meet in one position, two of them start reproducing process losing some energy to give a birth to a new animal spawning in this square with genotype based on parents. Plants spawns in random position every day. Simulation ends when all animals die.

Additional user can configure parameters of simulation in json file before start and start, pause or change speed of evolution process in any time.

## Used Technologies
- Swing
- AWT
- JSON file

## Problems
- ConcurrentModificationException or NoSuchElementException - view crashes if there are too many animals on map -> lack of knowledge about concurrent programming when it was implemented
- Statistics for every single animal weren't implemented
