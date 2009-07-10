package edu.gemini.aspen.gmp.broker.impl;

import edu.gemini.aspen.gmp.commands.api.HandlerResponse;
import edu.gemini.aspen.gmp.commands.api.CommandUpdater;
import edu.gemini.aspen.gmp.broker.commands.ActionManager;

/**
 *
 */
public class CommandUpdaterImpl implements CommandUpdater {

    private final ActionManager _manager;

    public CommandUpdaterImpl(ActionManager manager) {
        _manager = manager;
    }


    public void updateOcs(int actionId, HandlerResponse response) {
        //make the completion information available for the Action Manager
        //to notify the clients.
        _manager.registerCompletionInformation(actionId, response);
    }
}
