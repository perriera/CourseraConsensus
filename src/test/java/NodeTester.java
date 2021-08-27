import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;
// @RunWith attaches a runner with the test class to initialize the test data
@RunWith(MockitoJUnitRunner.class)
public class NodeTester {

    // @InjectMocks annotation is used to create and inject the mock object
    @InjectMocks
    Transaction transaction = new Transaction(10);

    // @Mock annotation is used to create the mock object to be injected
    @Mock
    Node node;

    @Test
    public void testAdd() {

        Set<Transaction> answer = new HashSet<Transaction>();
        boolean[] followees = { true, true };
        node.setFollowees(followees);
        when(node.sendToFollowers()).thenReturn(answer);

        Assert.assertEquals(node.sendToFollowers(), answer);
    }
}