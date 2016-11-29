package eu.solidcraft.film

import eu.solidcraft.base.IntegrationSpec
import eu.solidcraft.film.domain.FilmFacade
import eu.solidcraft.film.domain.FilmFacadeOperations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Ignore

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class FilmAcceptanceSpec extends IntegrationSpec implements FilmFacadeOperations {

    @Autowired
    FilmFacade filmFacade;

    @WithMockUser
    @Ignore
    def "positive scenario"() {
        given: 'inventory has an old film "American Clingon Bondage" and a new release of "50 shades of Trumpet"'
            filmFacade.add(trumper)
            filmFacade.add(clingon)

        when: 'I go to /films'
            ResultActions getFilms = mockMvc.perform(get("/films"))
        then: 'I see both films'
            getFilms.andExpect(status().isOk())
                .andExpect(content().json("""
                [
                    {"title":"$clingon","type":"OLD"},
                    {"title":"$trumper","type":"NEW"}
                ]
                """))

        when: 'I go to /film'
            ResultActions getFilm = mockMvc.perform(get("/film/$clingon"))
        then: 'I see details of the film'
            getFilm.andExpect(status().isOk())
                .andExpect(content().json("""
                [
                    {"title":"$clingon","type":"OLD"},
                ]
                """))
    }
}
