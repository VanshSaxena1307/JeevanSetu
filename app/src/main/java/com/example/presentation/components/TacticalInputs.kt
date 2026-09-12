package com.example.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.JeevanBrandGreen
import com.example.ui.theme.JeevanMedicalRed
import com.example.ui.theme.JeevanTextMuted
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun tacticalTextFieldColors(
    focusedBorderColor: Color = JeevanBrandGreen,
    unfocusedBorderColor: Color = Slate700,
    errorBorderColor: Color = JeevanMedicalRed,
    focusedContainerColor: Color = Slate900,
    unfocusedContainerColor: Color = Slate900
) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color(0xFFF1F5F9),
    disabledTextColor = Color(0xFF64748B),
    errorTextColor = Color(0xFFFCA5A5),
    focusedContainerColor = focusedContainerColor,
    unfocusedContainerColor = unfocusedContainerColor,
    disabledContainerColor = Slate900.copy(alpha = 0.5f),
    errorContainerColor = Slate900,
    cursorColor = JeevanBrandGreen,
    errorCursorColor = JeevanMedicalRed,
    selectionColors = TextSelectionColors(
        handleColor = JeevanBrandGreen,
        backgroundColor = JeevanBrandGreen.copy(alpha = 0.35f)
    ),
    focusedBorderColor = focusedBorderColor,
    unfocusedBorderColor = unfocusedBorderColor,
    disabledBorderColor = Color(0xFF1E293B),
    errorBorderColor = errorBorderColor,
    focusedLabelColor = focusedBorderColor,
    unfocusedLabelColor = Color(0xFF94A3B8),
    disabledLabelColor = Color(0xFF475569),
    errorLabelColor = JeevanMedicalRed,
    focusedPlaceholderColor = Color(0xFF64748B),
    unfocusedPlaceholderColor = Color(0xFF64748B),
    disabledPlaceholderColor = Color(0xFF334155),
    errorPlaceholderColor = Color(0xFF991B1B),
    focusedSupportingTextColor = Color(0xFF94A3B8),
    unfocusedSupportingTextColor = Color(0xFF64748B),
    disabledSupportingTextColor = Color(0xFF334155),
    errorSupportingTextColor = Color(0xFFFCA5A5),
    focusedPrefixColor = Color.White,
    unfocusedPrefixColor = Color(0xFF94A3B8),
    focusedSuffixColor = Color(0xFF94A3B8),
    unfocusedSuffixColor = Color(0xFF64748B)
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
    accentColor: Color = JeevanBrandGreen
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            label = if (label != null) {
                { Text(text = label, color = if (isError) JeevanMedicalRed else Color(0xFF94A3B8)) }
            } else null,
            placeholder = if (placeholder != null) {
                { Text(text = placeholder, color = Color(0xFF64748B)) }
            } else null,
            leadingIcon = if (leadingIcon != null) {
                { Icon(imageVector = leadingIcon, contentDescription = null, tint = accentColor) }
            } else null,
            trailingIcon = if (trailingText != null) {
                {
                    Text(
                        text = trailingText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            } else null,
            isError = isError,
            supportingText = if (errorMessage != null && isError) {
                { Text(text = errorMessage, color = Color(0xFFFCA5A5)) }
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
                unfocusedBorderColor = Slate700
            )
        )
    }
}
