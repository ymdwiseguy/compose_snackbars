# Handling Snackbars in Jetpack Compose with Material 3

Handling Snackbars with Jetpack Compose can be a bit tricky. Sadly you can not simply call something like `Snackbar.show("my message")`.

This is a tutorial, how to use snackbars with jetpack compose and implementing custom versions on your own.

Feel free to check out the code and build the sample app to see the result in action.

There is some work to be done, so lets get startet!

## The Setting

First of all, we need some frame where our Snackbar should be shown. Lets asume we are inside a compose context within a screen. The frame is provided by material3 in form of a `Scaffold`, so our screen might look like this:

```kt
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

To show snackbars, you need to provide a `SnackbarHost` to the Scaffold. The host requires a `SnackbarHostState`.

```kt
val snackbarHostState = remember { SnackbarHostState() }

Scaffold(    
    snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
)
```

The host state handles all your snackbars logic. It can trigger the snackbar via `snackbarHostState.showSnackbar("Snackbar message")`.

But for this we need a coroutine context. The coroutine is needed because showing a snackbar is more of an event than just a state:

- The snackbar should be shown only once (and not over and over again with every recomposition)
- It should disappear after a while 🤔

## Showing the snackbar

For the problems above, Jetpack Compose has a neat little tool called `LaunchedEffect`. The LaunchedEffect allows you to start something asynchronously, but only once. For this it requires a key to identify if the required effect is still the same. We can call our Snackbar via

```kt
LaunchedEffect(key) {
    snackbarHostState.showSnackbar("message")
}
```

So, what should we provide as a key? Since it is of type `Any?` we could just use our snackbar message. But what happens if I actually whant to show the same message a second time?

### View events

A possible solution and the proposal from my side is to having a data class for view events wich could look like this:

```kt
data class SnackBarViewEvent(
    val message: String,
    val eventId: UUID = UUID.randomUUID(),
)
```

This way you have your key in form of the `eventId` which identifies your event and makes sure it is only shown once. The view events are wrapped into a state which can be handled in your composable.

## First Result

Putting all the stuff together, our first result looks like this:

```kt
data class SnackBarViewEvent(
    val message: String,
    val eventId: UUID = UUID.randomUUID(),
)

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

Done! Right? Maybe not quite yet. So far we have a nice little working example. But what does your customer/product owner/ui designer say? I guess they want to have some styling for your snackbar. And even worse, they might want to have different types of snackbars 🫣

Yep, thats how it goes, but we can handle this!

## Custom Snackbars

So now we want to customize our Snackbars AND we whant to have different Types of them. The types might for example be depending on a severity level like INFO and ERROR. Lets define this severity as an enum:

```kt
enum class SnackbarSeverity{
    INFO, ERROR
}
```

Now we want to style our Snackbar depending on the severity. We keep it simple by using an elevated card view with different text- and border- colors. Feel free to add icons or whatever you like in your own implementation.

```kt
@Composable
fun CustomSnackbar(
    message: String,
    severity: SnackbarSeverity = INFO,
) {

    val color = when (severity) {
        INFO -> colors.onSurface
        ERROR -> colors.error
    }

    ElevatedCard(
        modifier = Modifier
            .padding(dimensions.gapS)
            .border(1.dp, color, shapes.small),
        shape = shapes.small
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(dimensions.gapL)
        ) {
            Text(text = message, color = color)
        }
    }
}
```

## Connecting the pieces

Now we need to bring the parts together. So far we know about the `SnackbarHost`, the `SnackbarHostState`, the `LaunchedEffect` and we have our own customized snackbar.

The SnackbarHost provides the snackbar in form of a function of type `@Composable (SnackbarData) -> Unit` with a default value of `{ Snackbar(it) }`. This is the place to put our own custom snackbar like this:

```kt
SnackbarHost(hostState = hostState) { snackbarData ->
    CustomSnackbar(
        message = snackbarData.visuals.message
    )
}
```

### SnackbarVisuals

As you can see, the `SnackbarData` provides visuals including the message. But our severity is still missing. We have to dig even deeper to achieve our goal. We have to implement our own `SnackbarVisuals` and add whatever property we need. In our case the severity:

```kt
data class CustomSnackbarVisuals(
    override val actionLabel: String?,
    override val duration: SnackbarDuration,
    override val message: String,
    override val withDismissAction: Boolean,
    val severity: SnackbarSeverity,
) : SnackbarVisuals
```

### CustomSnackbarHost

Combining the default SnackbarHost with the custom visuals looks like this:

```kt
@Composable
fun CustomSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) { snackbarData ->
        CustomSnackbar(
            message = snackbarData.visuals.message,
            severity = (snackbarData.visuals as? CustomSnackbarVisuals)?.severity ?: INFO
        )
    }
}
```

We try to cast the snackbars visuals to our own implementation and read the severity in case of success. In case of failure we provide a default value to avoid exceptions.

### A custom launcher

Finally we need to launch the snackbar and provide the severity and the other visuals (since we are overriding the default). For this we have our own launcher which we can call from wherever we like:

```kt
@Composable
fun LaunchCustomSnackbar(
    key: Any?,
    snackbarHostState: SnackbarHostState,
    message: String,
    severity: SnackbarSeverity,
) {
    LaunchedEffect(key){
        snackbarHostState.showSnackbar(
            visuals = CustomSnackbarVisuals(
                actionLabel = null,
                duration = SnackbarDuration.Short,
                message = message,
                withDismissAction = false,
                severity = severity,
            )
        )
    }
}
```

## Final Result

Our final snackbar sample screen now looks like this:

```kt
@Composable
fun SnackbarSampleScreen() {

    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarEvents: MutableState<SnackbarViewEvent?> = remember {
        mutableStateOf(null)
    }

    fun triggerSnackbar(message: String, severity: SnackbarSeverity) {
        snackBarEvents.value = SnackbarViewEvent(message, severity)
    }

    Scaffold(
        topBar = { TopAppBar({ Text("Snackbars") }) },
        snackbarHost = {
            CustomSnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Button(
                onClick = {
                    triggerSnackbar(
                        message = "This is a snackbar with the severity level INFO",
                        severity = INFO
                    )
                }
            ) {
                Text(text = "Show Snackbar 1")
            }
            Button(
                onClick = {
                    triggerSnackbar(
                        message = "This is a snackbar with the severity level ERROR",
                        severity = ERROR
                    )
                }
            ) {
                Text(text = "Show Snackbar 2")
            }
        }
    }

    snackBarEvents.value?.let {
        LaunchCustomSnackbar(
            key = it.eventId,
            snackbarHostState = snackbarHostState,
            message = it.message,
            severity = it.severity,
        )
    }
}
```

Now you know how to implement you own customized Snackbars with Jetpack Compose. Personally I find it quite complicated but this seems to be the intended way to go. On the bright side, you have to implement this stuff only once and can reuse it all over your app.

You can also define other stuff like an action to dismiss the snackbar in the visuals, the pattern is the same as above.

I hope this tutorial helps to understand how to use snackbars in Jetpack Compose. Please leave a star, if it does. 🙃
