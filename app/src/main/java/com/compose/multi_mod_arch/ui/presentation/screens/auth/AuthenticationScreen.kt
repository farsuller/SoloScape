package com.compose.multi_mod_arch.ui.presentation.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.compose.multi_mod_arch.ui.presentation.components.GoogleButton
import com.compose.multi_mod_arch.util.Constants.CLIENT_ID
import com.compose.multimodular.R
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import java.lang.Exception

@Composable
fun AuthenticationScreen(
    authenticated : Boolean,
    loadingState : Boolean,
    oneTapState : OneTapSignInState,
    messageBarState : MessageBarState,
    onButtonClicked : () -> Unit,
    onTokenReceived : (String) -> Unit,
    onDialogDismissed : (String) -> Unit,
    navigateToHome : () -> Unit
){
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
       content = {
           ContentWithMessageBar(messageBarState = messageBarState) {
               AuthenticationContent(
                   loadingState = loadingState,
                   onButtonClicked = onButtonClicked
               )
           }
       }
    )
    
    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            onTokenReceived(tokenId)
        },
        onDialogDismissed = { message ->
            onDialogDismissed(message)
        }
    )
    
    LaunchedEffect(key1 = authenticated){
        if(authenticated){
            navigateToHome()
        }
    }
}
