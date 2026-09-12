package com.example.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.MintDeep
import com.example.ui.theme.MintLight
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun tacticalTextFieldColors(
    focusedBorderColor: Color = MintDeep,
    unfocusedBorderColor: Color = BorderSubtle,
    errorBorderColor: Color = StatusDanger,
    focusedContainerColor: Color = SurfaceWhite,
    unfocusedContainerColor: Color = SurfaceWhite
) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    disabledTextColor = TextTertiary,
    errorTextColor = StatusCritical,
    focusedContainerColor = focusedContainerColor,
    unfocusedContainerColor = unfocusedContainerColor,
    disabledContainerColor = SurfaceSubtle,
    errorContainerColor = SurfaceWhite,
    cursorColor = MintDeep,
    errorCursorColor = StatusDanger,
    selectionColors = TextSelectionColors(
        handleColor = MintDeep,
        backgroundColor = MintLight
    ),
    focusedBorderColor = focusedBorderColor,
    unfocusedBorderColor = unfocusedBorderColor,
    disabledBorderColor = BorderSubtle,
    errorBorderColor = errorBorderColor,
    focusedLabelColor = focusedBorderColor,
    unfocusedLabelColor = TextSecondary,
    disabledLabelColor = TextTertiary,
    errorLabelColor = StatusDanger,
    focusedPlaceholderColor = TextTertiary,
    unfocusedPlaceholderColor = TextTertiary,
    disabledPlaceholderColor = TextTertiary,
    errorPlaceholderColor = StatusDanger,
    focusedSupportingTextColor = TextSecondary,
    unfocusedSupportingTextColor = TextSecondary,
    disabledSupportingTextColor = TextTertiary,
    errorSupportingTextColor = StatusDanger,
    focusedPrefixColor = TextPrimary,
    unfocusedPrefixColor = TextSecondary,
    focusedSuffixColor = TextSecondary,
    unfocusedSuffixColor = TextSecondary
)

@Composable
fun TacticalInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingText: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    accentColor: Color = MintDeep
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            ),
            label = if (label != null) {
                { Text(text = label, color = if (isError) StatusDanger else TextSecondary) }
            } else null,
            placeholder = if (placeholder != null) {
                { Text(text = placeholder, color = TextTertiary) }
            } else null,
            leadingIcon = if (leadingIcon != null) {
                { Icon(imageVector = leadingIcon, contentDescription = null, tint = accentColor) }
            } else null,
            trailingIcon = if (trailingText != null) {
                {
                    Text(
                        text = trailingText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            } else null,
            isError = isError,
            supportingText = if (errorMessage != null && isError) {
                { Text(text = errorMessage, color = StatusDanger) }
            } else null,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(12.dp),
            colors = tacticalTextFieldColors(
                focusedBorderColor = accentColor,
                unfocusedBorderColor = BorderSubtle
            )
        )
    }
}
