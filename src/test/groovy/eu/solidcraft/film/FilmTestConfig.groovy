package eu.solidcraft.film

import eu.solidcraft.film.domain.FilmFacade
import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spock.mock.DetachedMockFactory

@CompileStatic
@Configuration
class FilmTestConfig {
    DetachedMockFactory detachedMockFactory = new DetachedMockFactory();

    @Bean
    FilmFacade filmFacade() {
        return detachedMockFactory.Stub(FilmFacade)
    }
}
