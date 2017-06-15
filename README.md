# PCRSSBlog
A demo application that parses Personal Capital RSS feeds and displays them in all orientations.

## Architecture
MVP Architecture without any architectural framework.

## Screens
* Splash - Splash Screen
* Article List - Displays list of articles
* Article Detail - Displays article link in webview.

## Screen Implementation: Each screen is implemented using following classes and interfaces.
* A contract class which defines the connection between the view and the presenter.
* An Activity which instantiates views and presenters.
* A Fragment which implements the view interface.
* A presenter which implements the presenter interface in the corresponding contract.

A presenter typically hosts business logic associated with a particular feature, and the corresponding view handles the Android UI work. The view contains almost no logic; it converts the presenter's commands to UI actions, and listens for user actions, which are then passed to the presenter.

## Release
Run `./gradlew clean assembleRelease`