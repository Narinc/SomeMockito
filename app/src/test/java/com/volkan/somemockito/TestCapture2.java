package com.volkan.somemockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
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
}
