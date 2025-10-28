package com.starfruit.navtuto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PagesView(component: PagesComponent) {
    ChildPages(
        pages = component.pages,
        onPageSelected = component::selectPage,
        scrollAnimation = PagesScrollAnimation.Default,
        modifier = Modifier.fillMaxSize(),
        pager = { modifier, state, key, pageContent ->
            HorizontalPager(
                modifier = modifier,
                state = state,
                key = key,
                contentPadding = PaddingValues(start = 64.dp),
                pageContent = pageContent,
            )
        },
    ) { index, pageComponent ->
        PageView(pageComponent)
    }

    //indicator
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        val pagerState by component.pages.subscribeAsState()

        repeat(pagerState.items.size) { iteration ->
            val color =
                if (pagerState.selectedIndex == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(16.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PagesPreview() {
    MaterialTheme {
        PagesView(pagesComponentPreview)
    }
}

val pagesComponentPreview = PagesComponent(
    componentContext = componentContextPreview,
    pageComponentFactory = PageComponent.Factory()
)


//val pagesComponentPreview = object : PagesComponent {
//    override val pages: Value<ChildPages<*, PageComponent>>
//        get() = MutableValue(ChildPages(
//            items = listOf(
//                Child.Created(0, pageComponentPreview),
//                Child.Created(1, pageComponentPreview),
//            ),
//            selectedIndex = 1
//        ))
//    override fun selectPage(index: Int) {}
//}
