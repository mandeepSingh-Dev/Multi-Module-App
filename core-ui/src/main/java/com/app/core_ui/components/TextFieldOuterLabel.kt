package com.app.core_ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.grey_1
import com.app.core_ui.ui.theme.hint_color
import com.app.core_ui.ui.theme.hint_color_referral
import com.app.core_ui.ui.theme.red
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_12
import com.app.core_ui.ui.theme.theme_color_7
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20


@Composable
fun TextFieldOuterLabel(
    modifier: Modifier = Modifier,
    label: String? = null,
    error: String? = null,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    hint: String = "",
    hintColor: Color = hint_color,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isBorder : Boolean = false,
    borderColor : Color = theme_color_1,
    isRequired : Boolean = false,
    charsLimit : Int? = null,
    bgColor : Color = Color.White,
    supportingText : @Composable (() -> Unit)? = null,
    focusRequester: FocusRequester? = null,
    isPassword : Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showShadows : Boolean = false
)
{


    var isFocused by rememberSaveable { mutableStateOf(false)}

    var passwordVisible by rememberSaveable{ mutableStateOf(false) }

    Column(modifier = modifier) {

        label?.let {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black, fontFamily = HAFFER_TRIAL_MEDIUM)){
                        append(it)
                    }
                    if(isRequired){
                        withStyle(style = SpanStyle(color = red, fontFamily = HAFFER_TRIAL_MEDIUM)){
                            append(" *")
                        }
                    }
                },
                fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
                charsLimit?.let {
                    val length = charsLimit - value.length
                    Text(length.toString(), color = theme_color_1, fontSize = 12.sp)
                }
                }
        }

        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                textMotion = TextMotion.Animated, fontSize = 12.sp,
                color = Color.Black
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            minLines = minLines,
            enabled = enabled,
            readOnly = readOnly,
            leadingIcon = leadingIcon,
            trailingIcon = if(!isPassword) trailingIcon else {
                {
                    AnimatedContent (passwordVisible) { visible ->
                     if(visible) {
                         Image(
                             painterResource(R.drawable.baseline_visibility_off_24), contentDescription = null,
                             modifier = Modifier.clickable {
                                 passwordVisible = !passwordVisible
                             }
                         )
                     }else{
                         Image(
                             painterResource(R.drawable.baseline_visibility_24), contentDescription = null,
                             modifier = Modifier.clickable {
                                 passwordVisible = !passwordVisible
                             }
                         )
                     }
                    }
                }
            },
            supportingText = supportingText,
            placeholder = {
                Text(hint, color = hintColor, fontSize = 12.sp)
            },
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledTextColor = Color.Black
            ),
            visualTransformation = if (passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            modifier = Modifier
                .onFocusChanged{
                    isFocused = it.isFocused
                }
                .fillMaxWidth()
                .padding(top = if(!label.isNullOrEmpty()) 10.dp else 0.dp)
                .then(if(focusRequester != null){
                    Modifier.focusRequester(focusRequester)}else{
                        Modifier
                })
                .then(if(showShadows){
                    Modifier.dropShadow(
                        shape = Shape_15,
                        shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 5.dp)
                    )
                }else{
                    Modifier
                })

                .then(if(isBorder){
                    Modifier.border(width = 1.dp, color = if(isFocused) borderColor else borderColor.copy(alpha = 0.4f), shape = Shape_15)
                }else Modifier)
                .background(shape = Shape_15, color = bgColor)
        )

        AnimatedVisibility(!error.isNullOrEmpty()) {
            error?.let {
                Text(
                    it,
                    fontFamily = HAFFER_TRIAL_MEDIUM,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
@Composable
fun ReferralTextField(
    modifier: Modifier = Modifier,
    label: String? = null,
    error: String? = null,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    hint: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isBorder : Boolean = false,
    isRequired : Boolean = false,
    charsLimit : Int? = null,
    bgColor : Color = grey_1,
    supportingText : @Composable (() -> Unit)? = null,
    focusRequester: FocusRequester? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Characters,
        imeAction = ImeAction.Done,
        showKeyboardOnFocus = true,
        autoCorrect = false
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
)
{
    Column(modifier = modifier) {

        label?.let {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth())
            {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black, fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp)){
                        append(it)
                    }
                    if(isRequired){
                        withStyle(style = SpanStyle(color = red, fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp)){
                            append(" *")
                        }
                    }
                },
                fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
                charsLimit?.let {
                    val length = charsLimit - value.length
                    Text(length.toString(), color = theme_color_12, fontSize = 12.sp)
                }
                }
        }

        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                textMotion = TextMotion.Animated,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            minLines = minLines,
            enabled = enabled,
            readOnly = readOnly,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = supportingText,
            singleLine = true,
            placeholder = {

                Text(text = hint,
                    color = hint_color_referral.copy(alpha = 0.3f),
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally))
            },
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledTextColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp)
                .then(if(focusRequester != null){
                    Modifier.focusRequester(focusRequester)}else{
                        Modifier
                })
                .dropShadow(
                    shape = Shape_20,
                    shadow = Shadow(color = theme_color_1.copy(alpha = 0.1f), radius = 5.dp)
                )
                .then(if(isBorder){
                    Modifier.border(width = 1.dp, color = grey_1, shape = Shape_20)
                }else Modifier)
                .background(shape = Shape_20, color = bgColor)
        )

        AnimatedVisibility(!error.isNullOrEmpty()) {
            error?.let {
                Text(
                    it,
                    fontFamily = HAFFER_TRIAL_MEDIUM,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun TextFieldOuterLabel2(
    modifier: Modifier = Modifier,
    label: String? = null,
    error: String? = null,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    hint: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isBorder: Boolean = false,
    borderColor: Color = theme_color_1,
    isRequired: Boolean = false,
    charsLimit: Int? = null,
    bgColor: Color = Color.White,
    supportingText: @Composable (() -> Unit)? = null,
    focusRequester: FocusRequester? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIconAlignment: Alignment.Vertical = Alignment.Top,
    trailingIconAlignment: Alignment.Vertical = Alignment.CenterVertically,

    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showShadows: Boolean = false
) {

    var isFocused by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
    ) {

        // ---------------- LABEL ----------------

        label?.let {

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            )
            {

                Text(
                    buildAnnotatedString {

                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontFamily = HAFFER_TRIAL_MEDIUM
                            )
                        ) {
                            append(it)
                        }

                        if (isRequired) {
                            withStyle(
                                style = SpanStyle(
                                    color = red,
                                    fontFamily = HAFFER_TRIAL_MEDIUM
                                )
                            ) {
                                append(" *")
                            }
                        }
                    },
                    fontFamily = HAFFER_TRIAL_MEDIUM,
                    fontSize = 13.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                charsLimit?.let {

                    val length = charsLimit - value.length

                    Text(
                        length.toString(),
                        color = theme_color_1,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // ---------------- TEXT FIELD ----------------

        val fieldModifier = Modifier
            .fillMaxWidth()
            .padding(
                top = if (!label.isNullOrEmpty()) 10.dp else 0.dp
            )
            .then(
                if (focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else {
                    Modifier
                }
            )
            .then(
                if (showShadows) {
                    Modifier.dropShadow(
                        shape = Shape_15,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.1f),
                            radius = 5.dp
                        )
                    )
                } else {
                    Modifier
                }
            )
            .then(
                if (isBorder) {
                    Modifier.border(
                        width = 1.dp,
                        color = if (isFocused) {
                            borderColor
                        } else {
                            borderColor.copy(alpha = 0.4f)
                        },
                        shape = Shape_15
                    )
                } else {
                    Modifier
                }
            )
            .background(
                shape = Shape_15,
                color = bgColor
            )

        BasicTextField(
            value = value,
            onValueChange = {
                if (charsLimit == null || it.length <= charsLimit) {
                    onValueChange(it)
                }
            },
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            textStyle = TextStyle(
                textMotion = TextMotion.Animated,
                fontSize = 12.sp,
                color = Color.Black
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = fieldModifier
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            decorationBox = { innerTextField ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(
                            minHeight = 56.dp
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 12.dp
                        ),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {

                    // LEADING ICON

                    leadingIcon?.let {

                        Box(
                            modifier = Modifier
                                .align(leadingIconAlignment)
                        ) {
                            it()
                        }

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )
                    }

                    // TEXT

                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {

                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                color = hint_color,
                                fontSize = 12.sp
                            )
                        }

                        innerTextField()
                    }

                    // TRAILING ICON

                    trailingIcon?.let {

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .align(trailingIconAlignment)
                        ) {
                            it()
                        }
                    }
                }
            }
        )

        // ---------------- SUPPORTING TEXT ----------------

        supportingText?.invoke()

        // ---------------- ERROR ----------------

        AnimatedVisibility(
            visible = !error.isNullOrEmpty()
        ) {
            error?.let {

                Text(
                    text = it,
                    fontFamily = HAFFER_TRIAL_MEDIUM,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownTextField(
    modifier: Modifier = Modifier,
    list: List<T>,
    getText: (T) -> String?,
    label: String? = null,
    error: String? = null,
    value: String = "",
    onSelect: (T) -> Unit,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    isRequired : Boolean = false,
    hint: String = "",
    enabled: Boolean = true,
    fontSize: TextUnit = 12.sp,
    showDropDown : Boolean = false,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
) {
    var showDropDownMenu by rememberSaveable { mutableStateOf(showDropDown) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {

        label?.let {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black, fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp)){
                        append(it)
                    }
                    if(isRequired){
                        withStyle(style = SpanStyle(color = red, fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp)){
                            append(" *")
                        }
                    }
                },
                fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp,
                color = Color.Black,
            )
        }
        ExposedDropdownMenuBox(showDropDownMenu, onExpandedChange = {
            showDropDownMenu = false
        }, modifier = if(enabled){
            Modifier.clickableAnim {
                keyboardController?.hide()
                showDropDownMenu = !showDropDownMenu
            }
        }
        else {Modifier}
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .dropShadow(shape = Shape_15, shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 5.dp))
                    .border(width = 1.dp, shape = Shape_15, color = theme_color_1)
                    .background(shape = Shape_15, color = Color.White)
                    .padding(vertical = 12.dp, horizontal = 13.dp)
            ) {
                leadingIcon?.let {
                    Image(painter = painterResource(leadingIcon), contentDescription = null)
                }
                AnimatedContent(
                    value, modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) { value ->
                    if (value.isNullOrEmpty()) {
                        Text(hint, fontSize = fontSize, color = hint_color)
                    } else {
                        Text(value, fontSize = fontSize, color = Color.Black)
                    }
                }
                trailingIcon?.let {
                    Image(painter = painterResource(trailingIcon), contentDescription = null)
                }
            }
            ExposedDropdownMenu(
                showDropDownMenu, matchAnchorWidth = true,
                onDismissRequest = {
                    showDropDownMenu = false
                },
                containerColor = theme_color_7,
                shadowElevation = 10.dp,
                shape = Shape_20,
                modifier = Modifier.background(color = theme_color_7, shape = Shape_20)
                    .innerShadow(Shape_20, shadow = Shadow(radius = 10.dp, color = theme_color_12.copy(alpha = 0.5f))),
                scrollState = rememberScrollState()
            ) {

                list.forEach {
                    getText(it)?.let { text ->
                        Text(
                            text, fontSize = fontSize, modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showDropDownMenu = false
                                    onSelect(it)
                                }
                                .padding(
                                    horizontal = 20.dp, vertical =
                                        10.dp
                                ))
                    }
                }
            }
        }

        AnimatedVisibility(!error.isNullOrEmpty(), modifier = Modifier.fillMaxWidth().padding(top = 5.dp, start = 5.dp, end = 5.dp)) {
            Text(error ?: "", color = Color.Red, fontFamily = HAFFER_TRIAL_SEMI_BOLD, fontSize = 13.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> EditableDropDownTextField(
    modifier: Modifier = Modifier,
    list: List<T> ,
    getText: (T) -> String?,
    label: String? = null,
    error: String? = null,
    value: String = "",
    onValueChange: (String) -> Unit,
    onSelect: (T) -> Unit,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    hint: String = "",
    enabled: Boolean = true,
    fontSize: TextUnit = 16.sp,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
) {
    var showDropDownMenu by rememberSaveable(list) { mutableStateOf(list.isNotEmpty()) }


    Column(modifier = modifier) {

        label?.let {
            Text(
                it,
                fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp,
                color = Color.Black
            )
        }
        ExposedDropdownMenuBox(true, onExpandedChange = {
            showDropDownMenu = false
        }, modifier = Modifier) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .dropShadow(
                        shape = Shape_20,
                        shadow = Shadow(color = theme_color_1.copy(alpha = 0.3f), radius = 5.dp)
                    )
                    .background(shape = Shape_20, color = grey_1)
                    .padding(vertical = 10.dp, horizontal = 13.dp)
            ) {
                leadingIcon?.let {
                    Image(painter = painterResource(leadingIcon), contentDescription = null)
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    decorationBox = { innerTextField ->
                        if (value.isNullOrEmpty()) {
                            Text(hint, color = Color.Gray)
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
                trailingIcon?.let {
                    Image(painter = painterResource(trailingIcon), contentDescription = null)
                }
            }
            ExposedDropdownMenu(
                showDropDownMenu, matchAnchorWidth = true,
                onDismissRequest = {
                    showDropDownMenu = false
                },
                containerColor = theme_color_7,
                shadowElevation = 10.dp,
                shape = Shape_20,
                scrollState = rememberScrollState()
            ) {
                list.forEach {
                    getText(it)?.let { text ->
                        Text(
                            text, fontSize = fontSize, modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showDropDownMenu = false
                                    onSelect(it)
                                }
                                .padding(
                                    horizontal = 20.dp, vertical =
                                        10.dp
                                ))
                    }
                }
            }
        }
    }
}

