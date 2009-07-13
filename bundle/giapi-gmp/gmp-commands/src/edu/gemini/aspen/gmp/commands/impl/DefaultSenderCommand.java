package edu.gemini.aspen.gmp.commands.impl;

import edu.gemini.aspen.gmp.commands.Action;
import edu.gemini.aspen.gmp.commands.api.HandlerResponse;
import edu.gemini.aspen.gmp.commands.ActionSender;
import edu.gemini.aspen.gmp.commands.ActionMessage;
import edu.gemini.aspen.gmp.commands.SequenceCommandExecutor;

/**
 * The default sequence command executor. It will just send the
 * action to the instrument and will return the corresponding answer.
 */
public class DefaultSenderCommand implements SequenceCommandExecutor {

    private ActionMessageBuilder _actionMessageBuilder = new ActionMessageBuilder();

    public HandlerResponse execute(Action action, ActionSender sender) {

        ActionMessage m = _actionMessageBuilder.buildActionMessage(action);

        return sender.send(m);

    }
    


}
