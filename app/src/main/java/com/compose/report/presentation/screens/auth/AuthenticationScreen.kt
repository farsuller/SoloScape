package com.compose.report.presentation.screens.auth

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import com.compose.report.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

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
