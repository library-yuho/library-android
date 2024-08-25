package com.project.ibooku.presentation.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.theme.Black
import com.project.ibooku.presentation.ui.theme.Gray20
import com.project.ibooku.presentation.ui.theme.Gray30
import com.project.ibooku.presentation.ui.theme.Gray40
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.StarYellow
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr


@Composable
fun BaseHeader(modifier: Modifier = Modifier, headerTitle: String = "", onBackPressed: () -> Unit) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(White)
    ) {
        Row(
            modifier = modifier
                .background(White)
                .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    tint = Gray50,
                    contentDescription = null
                )
            }

            if (headerTitle.isNotEmpty()) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = headerTitle,
                    color = Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosanskr,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                )
            }

            Spacer(modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
fun BaseLoadingIndicator(
    isLoading: Boolean,
    color: Color = SkyBlue10,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = color)
        }
    }
}

@Composable
fun BaseDialog(
    title: String,
    msg: String = "",
    onPositiveRequest: () -> Unit,
    onNegativeRequest: (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(),
) {
    Dialog(
        onDismissRequest = onPositiveRequest,
        properties = properties
    ) {
        Card(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                ) {

                    Text(
                        text = title,
                        color = Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = notosanskr,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (msg.isNotEmpty()) {
                        Text(
                            text = msg,
                            color = Gray40,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = notosanskr,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (onNegativeRequest != null) {
                        TextButton(
                            modifier = Modifier
                                .weight(1f)
                                .background(Gray20),
                            contentPadding = PaddingValues(vertical = 15.dp),
                            onClick = onNegativeRequest
                        ) {
                            Text(
                                text = stringResource(id = R.string.common_close),
                                color = Gray40,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = notosanskr,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    }

                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .background(SkyBlue10),
                        contentPadding = PaddingValues(vertical = 15.dp),
                        onClick = onPositiveRequest
                    ) {
                        Text(
                            text = stringResource(id = R.string.common_done),
                            color = White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = notosanskr,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BaseButton(
    text: String,
    modifier: Modifier = Modifier,
    margin: PaddingValues = PaddingValues(0.dp),
    contentPadding: PaddingValues = PaddingValues(20.dp),
    backgroundColor: Color = SkyBlue10,
    disabledBackgroundColor: Color = Gray50,
    textColor: Color = White,
    border: Dp = 0.dp,
    round: Dp = 0.dp,
    borderColor: Color = SkyBlue10,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(modifier = modifier.padding(margin)) {
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (border.value > 0) {
                        Modifier.border(
                            width = border,
                            color = borderColor,
                            shape = RoundedCornerShape(round)
                        )
                    }else{
                        Modifier
                    }
                )
                .clip(RoundedCornerShape(round))
                .background(if (enabled) backgroundColor else disabledBackgroundColor),
            shape = if (round.value > 0) RoundedCornerShape(round) else RectangleShape,
            contentPadding = contentPadding,
            enabled = enabled,
            onClick = onClick
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
}


@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Float,
) {
    val density = LocalDensity.current.density
    val starSize = (8f * density).dp
    val starSpacing = (0.5f * density).dp

    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Default.Star
            val iconTintColor = if (isSelected) StarYellow else Gray30
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .width(starSize)
                    .height(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}