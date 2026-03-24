package com.company.crm.ai.view.aiconversation;

import com.company.crm.ai.model.AiConversation;
import com.company.crm.ai.service.AiConversationService;
import com.company.crm.app.util.constant.CrmConstants;
import com.company.crm.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.Messages;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "ai-conversations", layout = MainView.class)
@ViewController(id = CrmConstants.ViewIds.AI_CONVERSATION_LIST)
@ViewDescriptor(path = "ai-conversation-list-view.xml")
@LookupComponent("aiConversationsDataGrid")
@DialogMode(width = "90%", resizable = true)
public class AiConversationListView extends StandardListView<AiConversation> {

    @Autowired
    private Messages messages;
    @Autowired
    private ViewNavigators viewNavigators;
    @Autowired
    private AiConversationService aiConversationService;

    @Subscribe("aiConversationsDataGrid.createAction")
    public void onAiConversationsDataGridCreateAction(ActionPerformedEvent event) {
        AiConversation savedConversation = createConversationWithWelcomeMessage();
        viewNavigators.detailView(this, AiConversation.class)
                .editEntity(savedConversation)
                .navigate();
    }

    private AiConversation createConversationWithWelcomeMessage() {
        String welcomeMessage = messages.getMessage("aiConversation.welcomeMessage");
        return aiConversationService.createNewConversation(welcomeMessage);
    }
}
