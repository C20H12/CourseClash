//Mahir
package interface_adapter.MultiPlayer.receive_message;

import use_case.MultiPlayer.receive_message.ReceiveMessageInputBoundary;

public class ReceiveMessageController {
    final ReceiveMessageInputBoundary interactor;

    public ReceiveMessageController(ReceiveMessageInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String data) {
        interactor.execute(data);
    }
}
