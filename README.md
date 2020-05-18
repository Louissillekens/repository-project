# repository-project
repository for the putting game project

Since the entire libGDX set-up becomes quite cluttered we decided it best to write a short manual on how to use the given code

First of, to run the game as intended just run the DesktopLauncher under desktop/src/com.game.game.desktop/DesktopLauncher
this starts the game up and you can choose from a few options within the menu

other than that you can find all of our actual code within the core folder, in here the src folder containing our code is situated
All assets are also stored within core

within src you can find, easily divided by folders, the code we currently use, the game class that was automatically generated,
and the old code (we kept all of our old code around for completion)

when looking through the various folders in src/code there are also several classes which can be executed on their own
For instance in src/code/physics there are a few solvers that can be tested without running the game
Inside OldEdits and NeuralNet there are several classes that also have a run option of their own

If you wish to change the course to golf on this can be done within class PuttingGameScreen for now (method defineFunction : line 196)
this is one thing we still wish to change in the last period, the user should be able to define a function themselves
