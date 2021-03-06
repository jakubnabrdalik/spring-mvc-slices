package eu.solidcraft.film

import eu.solidcraft.base.IntegrationSpec
import eu.solidcraft.film.domain.FilmFacade
import eu.solidcraft.film.domain.FilmFacadeOperations
import org.junit.After
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.ResultActions

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class FilmControllerSpec extends IntegrationSpec implements FilmFacadeOperations {

    @Autowired
    FilmFacade filmFacade

    @After
    void removeFilms() {
        filmFacade.delete(trumper.title, clingon.title)
    }

    @WithMockUser
    def "should get films"() {
        given: 'inventory has "American Clingon Bondage"'
            filmFacade.add(trumper)
            filmFacade.add(clingon)

        when: 'I go to /film'
            ResultActions getFilms = mockMvc.perform(get("/films"))

        then: 'I see details'
            getFilms.andExpect(status().isOk())
                .andExpect(content().json("""
                { 
                    "content": [
                        {"title":"$clingon.title","type":"$clingon.type"},
                        {"title":"$trumper.title","type":"$trumper.type"}
                    ]
                }""", false))
    }

    @WithMockUser
    def "should get film"() {
        given: 'inventory has an old film "American Clingon Bondage" and a new release of "50 shades of Trumpet"'
            filmFacade.add(clingon)

        when: 'I go to /films'
            ResultActions getFilm = mockMvc.perform(get("/film/$clingon.title"))

        then: 'I see film'
            getFilm.andExpect(status().isOk())
                .andExpect(content().json("""
                        {"title":"$clingon.title","type":"$clingon.type"},
                """, false))
    }
}
