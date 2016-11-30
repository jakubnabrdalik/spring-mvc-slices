# test for how fast slices are

---

This is an example of how MVC slice testing in Spring works with Spock (snapshot version of spring 5)

This helped find a bug (or a feature) of spock-spring, that made spring not cache mvc slice contexts as expected.

See https://github.com/spring-projects/spring-boot/issues/7524

---

First commit runs on slices, here is the outcome

![slices](https://github.com/jakubnabrdalik/spring-mvc-slices/raw/master/onSlices.png)

You can notice that context, as expected, gets up in two phases: mvc slice needing just "half" of it, and the the integration test needing another "half".

What is troublesome though, is that the second MVC slice test, which uses the same context as the first, still takes >1s to start. And that's for the first test in the class. The second test runs fast, as expected.

Second commit runs all tests as integration test.

![slices](https://github.com/jakubnabrdalik/spring-mvc-slices/raw/master/allIntegration.png)

You can see that after the context gets up, all tests are fast.
