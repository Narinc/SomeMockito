package com.volkan.somemockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by volkannarinc on 20.03.2018 16:42.
 */

public class TestCapture2 {
    @Mock
    private LinkedList mockedList;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void stubbingConsecutiveCalls() {
        //You can mock concrete classes, not just interfaces
        when(mockedList.get(0))
                .thenThrow(new RuntimeException())
                .thenReturn("foo");

        when(mockedList.get(1))
                .thenReturn("foo", "jedi", "lotr");

        // if instead of chaining .thenReturn() calls, multiple stubbing with the same matchers or
        // arguments is used, then each stubbing will override the previous one
        when(mockedList.get(2))
                .thenReturn("harry");

        when(mockedList.get(2))
                .thenReturn("hermony");

        System.out.println(mockedList.get(2));

        //First call: print "foo"
        System.out.println(mockedList.get(1));

        //Second call: print "jedi"
        System.out.println(mockedList.get(1));

        //Thiird call: print "lotr"
        System.out.println(mockedList.get(1));

        //First call: throws runtime exception:
        System.out.println(mockedList.get(0));

        //Second call: prints "foo"
        System.out.println(mockedList.get(0));

        //Any consecutive call: prints "foo" as well (last stubbing wins).
        System.out.println(mockedList.get(0));
    }

    @Test
    public void stubbingWithCallbacks() {
        when(mockedList.get(anyInt())).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Object mock = invocation.getMock();
                return "called with arguments: " + args;
            }
        });

        //the following prints "called with arguments: foo"
        System.out.println(mockedList.get(0));
        System.out.println(mockedList.get(0));
        System.out.println(mockedList.get(1));
    }

    @Test
    public void doSomething() {
        Mockito.doThrow(new RuntimeException())
                .when(mockedList).clear();

        //following throws RuntimeException:
        mockedList.clear();
    }

    @Test
    public void spyingOnReaalObjects() {

        List list = new LinkedList();
        List spy = Mockito.spy(list);

        //optionally, you can stub out some methods:
        when(spy.size()).thenReturn(100);

        //using the spy calls *real* methods
        spy.add("one");
        spy.add("two");

        //prints "one" - the first element of a list
        System.out.println(spy.get(0));

        //size() method was stubbed - 100 is printed
        System.out.println(spy.size());

        //optionally, you can verify
        verify(spy).add("one");
        verify(spy).add("two");

    }

    @Test
    public void gotchaOnSpyingRealObjects() {
        List list = new LinkedList();
        List spy = Mockito.spy(list);

        //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
        when(spy.get(0)).thenReturn("foo");

        //You have to use doReturn() for stubbing
        Mockito.doReturn("foo").when(spy).get(0);
    }

    @Test
    public void capturingForFurtherAssertions() {
        Email email = mock(Email.class);
        email.setBody("Naber");

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(email).setBody(argument.capture());
        assertEquals("Naber", argument.getValue());
    }

    @Test
    public void realPartialMocks(){
        List list = spy(new LinkedList());

        Email email = mock(Email.class);

        Mockito.when(email.setBody("naber")).thenCallRealMethod();
    }
}
