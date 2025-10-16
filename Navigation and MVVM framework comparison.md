---
Update: 2025-10-16
---

## projects health comparison
* [voyager](https://github.com/adrielcafe/voyager) - 4 years old -only 243 commits, 186 opened issues, no new release since 1 year (Last release was 7 Oct 2024)
* [PreCompose](https://github.com/Tlaster/PreCompose)  - 4 years old - 660/713 real commits, 40 opened issues, no new release since 1 year (Last release was 8 Sept 2024)
* [circuit](https://slackhq.github.io/circuit/navigation/) of Slack - 3 years old - seems active and well maintained with 
	* 616/2003 real commits (other commit comes from robot), 
	* 10 issues,  
	* last release 2 Aug 2025
	* 👎 Still in beta (version 0.30.0) after 3 years.
* [decompose](https://github.com/arkivanov/Decompose) of Twitter - 5 years old - the most active and maintained, 
	* 1721 real commits, 
	* 0 opened issues
	* last release 2 weeks ago
* [Appyx](https://bumble-tech.github.io/appyx/faq/) of Bumble - 4 years old, moderately active and maintained: 
  	* 1758 real commits, 67 opened issues, last release 2 May 2025.

Remark:
* voyager and preCompose seems abandoned!
* Appx solely focused on navigation => woth to take a look
* decompose and circuit stands out as both MVVM framework + navigation solution => let's focus on them first.

### decompose
* It add a abstraction layer, even own a reactivity system 
	* adding 👎 complexity + 👎 verbosity + 👎 steeper learning curve but 👍 offer clarity (no magic)
	* 👍 Doesn't depend on anything (evens the Compose API) so the Decompose's API is stable, learn once works anywhere
	* 👍 the most suitable for cross-platform development (thanks to this abstraction layer)
	* 👍 the best testability (thanks to this abstraction layer)
* 👍 work the same for any kind of UI (compose, android, desktop, iOS, web)
* 🆗 Component is in fact the ViewModel
* 🆗 BloC pattern from Flutter
 
## circuit	
* 🆗 compose UI only => heavily android, desktop, iOS via sikya (not native), Web experimental
* 👎Circuit's Screen requires the Android's `@Parcelize`, and the `Parcelable` interface. Devs have 2 choices
	* First choice: Screen implementations per platform specific.
		* Note: the "screen" is just a data class used as key and to hold the screen parameters, it is mostly fine to copy / paste it to different platform => but still this choice require (a little of) codes-duplication.
	* Second choice: Setup a multi-platform `@Parcelize` alias via `gradle` build's hack so that the Screen implementation can stays in `commonMain` (checkout the "star" example)
* 👍 The composable `Presenter` based on existing (and familiar) compose reactivity system. So 👎 it can use `rememberSavable` which is Android's-specific-made-for-multi-platform
* 🆗 Presenter is in fact the ViewModel (not the Presenter in Model-View-Presenter)
* 👍 Support Codes Generation (`@CircuitInject`) to avoid verbosity

## "standard" navigation solution
* Android jetpack navigation [nav2](https://developer.android.com/guide/navigation/design#compose)
	* declarative navigation, we "design a navigation graph"  
	
* Android jetpack navigation [nav3](https://android-developers.googleblog.com/2025/05/announcing-jetpack-navigation-3-for-compose.html)
	* Nav3 moves away from the rigid, XML-first **Navigation Graph** concept of Nav2 that you referenced, and instead focuses entirely on the **state of the back stack**.
	* Solving Adaptive Display with a Declarative Approach
		* The back stack remains the **single source of truth** for all screen sizes
		* Nav3 introduces the **Scenes API**, which is a flexible layout layer that looks at the current back stack state and the current **window size class** (e.g., compact, medium, expanded).
		* The Scene then **declares** the layout based on these two inputs:
			* - **Small Screen (Compact):** The Scene might only render the **topmost** item of the back stack, showing one screen at a time (single-pane phone UI).
			* **Large Screen (Expanded):** The Scene might render the **top two or three** items of the back stack simultaneously in a side-by-side layout, creating a list-detail or multi-pane view.
* [jetbrains's navigation-compose](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation.html#alternative-navigation-solutions) 
	* inspired from jetpack navigation for KMP, should be similar (to be confirmed)

