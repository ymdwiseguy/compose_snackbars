# Handling Snackbars in Jetpack Compose with Material 3

Handling Snackbars with Jetpack Compose can be a bit tricky. Sadly you can not simply call something like `Snackbar.show("my message")`. There is some work to be done, so lets get startet!

## The Setting

First of all, we need some frame where our Snackbar should be shown. Lets asume we are inside a compose context within a screen. The frame is provided by material3 in form of a `Scaffold`, so our screen might look like this:

```
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackbarSampleScreen() {

    fun triggerSnackbar(){

    }

    Scaffold(
        topBar = { TopAppBar({ Text("Snackbars") }) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Button(onClick = { triggerSnackbar() }) {
                Text(text = "Show Snackbar")
            }
        }
    }
}
```

## The Snackbar Host and its state

To show snackbars, you need to provide a `SnackbarHost` for the Scaffold. The host again requires a `SnackbarHostState`.
```
val snackbarHostState = remember { SnackbarHostState() }

Scaffold(    
    snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
)
```
The host state handles all your snackbar logic. It can trigger the snackbar via `snackbarHostState.showSnackbar("Snackbar message")`. But for this it needs a coroutine context!
The coroutine is necessary because we require an event in a state driven environment:
- The snackbar should be shown only once (and not over and over again with every recomposition)
- It should disappear after a while 🤔


## Showing the snackbar

For the problems above, Jetpack Compose has a neat little tool called `LaunchedEffect`. The LaunchedEffect allows you to start something, for example showing a snackbar inside a coroutine, but only once. For this it requires a key to identify if the required effect is still the same. We can call our Snackbar via
```
LaunchedEffect(key) {
    snackbarHostState.showSnackbar("message")
}
```
So, what should we provide as a key? Since it is of type `Any?` we could just use our snackbar message. But what happens if I actually whant to show the same message a second time?

### View events

A possible solution and the proposal from my side is having a data class for view events wich could look like this:
```
data class SnackBarViewEvent(
    val message: String,
    val eventId: UUID = UUID.randomUUID(),
)
```
This way you have your key in form of the `eventId` which identifies your event and makes sure it is only shown once. The view events are wrapped into a state which can be handled in your composable.

## First Result

Putting all the stuff together, our result looks like this:
```
data class SnackBarViewEvent(
    val message: String,
    val eventId: UUID = UUID.randomUUID(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackbarSampleScreen() {

    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarEvents: MutableState<SnackBarViewEvent?> = remember { mutableStateOf(null) }

    fun triggerSnackbar(message: String) {
        snackBarEvents.value = SnackBarViewEvent(message)
    }

    Scaffold(
        topBar = { TopAppBar({ Text("Snackbars") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Button(onClick = { triggerSnackbar("my first message") }) {
                Text(text = "Show Snackbar 1")
            }
            Button(onClick = { triggerSnackbar("my second message") }) {
                Text(text = "Show Snackbar 2")
            }
        }
    }

    snackBarEvents.value?.let {
        LaunchedEffect(it.eventId){
            snackbarHostState.showSnackbar(it.message)
        }
    }
}
```

## And now?

Done! Right? Maybe not quite yet. So far we have a nice little working example. But what does your customer/product owner/ui designer say? I guess they whant to have some styling for your snackbar. And even worse, they want to have different types of snackbars 🫣 Yep, thats business but we can handle this!

### Custom Snackbars

TODO



### Notes:

- Styling the snackbar
- providing more than just a message
