<img src ="https://github.com/user-attachments/assets/ead85ec9-5061-4f36-b0c2-e9f0c7054a19" width = 250px height= 200px ><br>
# Description
As the name suggests it is a crypto tracking application which fetches over 100 currencies from API and their price history,market cap,etc.
<br>The price changes are visualized through graphs which are implemented with the help of <b>CANVAS</b> in Jetpack Compose.
## Architecture
The application follows MVI android architecture.
## Theme
Material3 is used for dynamic application theme. Thus the app automatically adjusts to your phone's background
## Technology and libraries used -
* Ktor - Ktor is used for making API calls.
* Koin - It is a library used for dependency injection.
* Jetpack Compose - For app UI
* Kotlin - App logic
# Screenshots
## Home Page & Details Screen
<img src ="https://github.com/user-attachments/assets/67f8ea53-7026-4f4f-9d75-f8e2cbfcd49c" width = 500px height= 1000px>
<img src ="https://github.com/user-attachments/assets/b9d5e98a-22db-4fc4-9261-19f280f7ae1f" width = 500px height= 1000px>
<br>

## Horizontal Side by Side View
![Horizontal](https://github.com/user-attachments/assets/683d58dd-3dae-486b-8c00-0696dfe0e48f)

## List-Detail layout of the application 
This type of approach is usually used when your application has only two screens, e.g. a list screen and a detail screen.<br>
When enough space is available the two screens will be displayed side by side.<br>
This layout comes in very handy for tablets as the screen size is quite big.
<br><br>
My application uses the <b>NavigableListDetailPaneScaffold </b>composable by material3 library. It supports navigation and back handling out of the box.
## Graph with canvas
The application displays the ultimate power of canvas in Jetpack Compose.
This is probably the most complex part of this application.<br><br>
There are numerous calculations for figuring out the position of X & Y labels, spacing between them, their height-width, number of points visible depending upon the screen dimensions, maximum and minimum label width etc.<br>
A basic line curve logic is used for drawing a smooth curve between two consecutive points. The curves can be made even smoother with the help of Bezier Curves.
## API 
https://docs.coincap.io/
## Resources
I followed a tutorial by Philipp Lackner for this application, the guy's a genius.
