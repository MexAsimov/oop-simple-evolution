# oop-simple-evolution
[AGH-LAB] Simple project checking basic skills in Java OOP.

## Application Appearance

![image](https://user-images.githubusercontent.com/58474974/143791899-ee571bda-697c-4819-b7f8-c8115b604492.png)

Two seperated worlds created to show process of simple evolution. Animals (blue squares) are randomly moving over the map and eating Plants (green squares). If animal doesn't gather enough energy from plants his energy runs to zero and animal die. If more than one animal meet in one position, two of them start reproducing process losing some energy to give a birth to a new animal spawning in this square with genotype based on parents. Simulation ends when all animals die.

## Used Technologies
- Swing
- AWT
- JSON file

## Problems
- ConcurrentModificationException or NoSuchElementException - view crashes if there are too many animals on map
- Statistics for every single animal and whole map weren't implemented
