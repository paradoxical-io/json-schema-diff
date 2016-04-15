package io.paradoxical;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Random;

public class TestBase {
    protected static Random random = new Random();

    protected static PodamFactory fixture = new PodamFactoryImpl();
}
