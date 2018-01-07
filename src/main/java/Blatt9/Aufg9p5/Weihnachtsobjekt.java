package Blatt9.Aufg9p5;

/*
     _   _
    ( >o< )                         _
    ,%%/'^`\%%.                      (@)                       _
    %%'V   V`%%      6               |-|\____________________ (@)
    .     %%.     ,%%     ,~.              | |                     \|-|
    |     *%%.   ,%%*     | |              |-|   Happy Holidays!    | |
    |       *%%%%%*       | |      ____    |_|\___________________  |-|
    =l-------------------l===l----(oooo)   (@)                    `\|_|
    _______________________________|--|     ~                       (@)
    ================,==============|  |                              ~
    ============,,~'')=============|  |
    -------,,~''......)------------|  |
    ('............)           |  |        ,
    (........,###            |  |        I\,
    (....,#######           | __     ,_/__ \__A
    ``###########          |/'o\,   1 @, `/ ,/    ___,
    '  ###########         |I    `-''   ,  /__,--'_,-\
    ###########         |\>--.______/  /V     /
    `    ###########          |  |   `---._  \`\==. `-.
    ##########            |  |/\,--==,.) ,\\  \==. \
    #######  ,           |  /__\    `\`-. \)  _ \=.\
    ) ,*****               |  |        (`=,`-.'/ `),`-)
    / \( /  \               |  |     __(_(  \=,`--.'
    ( {/ \ ,\ )              |  |  ,=--(__,\   i\=, `-.
    \(\6/\\//               |  |  `//'\__  `  I  \=,  `-.
    /============\             |  |   ``(__ `-._,' __ \=,   \
    ______________________________(elya)    (__,     ,'  `-\=,  .\
    \     (___,  (       )=,  ,)
    ====================================I  ,--`(_,--.`_..  /=,  ,/
    ====================================I /w_,--/w_,--.__,(==__,'
*/
public abstract class Weihnachtsobjekt {
  protected int x, y;
  protected boolean fallend = true;
  protected boolean markedForDeath = false;

  protected int[][] spielfeld;

  public Weihnachtsobjekt(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public abstract void addObjektToSpielfeld(int[][] spielfeld);
  public abstract boolean moveDown(boolean[][] staticObjects);
  public abstract int moveLeft(boolean[][] staticObjects);
  public abstract int moveRight(boolean[][] staticObjects);
}
