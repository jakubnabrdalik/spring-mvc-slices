package eu.solidcraft

import eu.solidcraft.base.IntegrationSpec
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Ignore

import java.time.Clock
import java.time.Instant

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Ignore
class AcceptanceSpec extends IntegrationSpec {

    String clingon = "American Clingon Bondage"
    String trumper = "50 shades of Trumpet"
    Clock clock = Stub() {
        instant() >> Instant.parse("2000-01-01T01:01:01.00Z")
    }

    @WithMockUser
    def "positive renting scenario"() {
        given: 'inventory has an old film "American Clingon Bondage" and a new release of "50 shades of Trumpet"'
            //we will leave it for now

        when: 'I go to /films'
            ResultActions getFilms = mockMvc.perform(get("/films"))
        then: 'I see both films'
            getFilms.andExpect(status().isOk())
                    .andExpect(content().json("""
                    [
                        {"title":"$clingon","type":"OLD"},
                        {"title":"$trumper","type":"NEW"}
                    ]"""))


        when: 'I go to /points'
        then: 'I see I have no points'

        when: 'I post to /calculate with both films for 3 days'
        then: 'I can see it will cost me 120 SEK for Trumpet and 90 SEK for Clingon'

        when: 'I post to /rent with both firms for 3 days'
            ResultActions postRent = mockMvc.perform(
                    post("/rent")
                            .param("title", clingon, trumper)
                            .param("days", 3))
                    .andExpect(status().isOk())
        then: 'I have rented both movies'
            postRent.andExpect(status().isOk())
                    .andExpect(content().json("""
                    [{
                            "due",
                            "titles": ["$trumper","$clingon"]
                    }]"""))


        when: 'I go to /rent'
        then: 'I see both movies are rented'

        when: 'I go to /points'
        then: 'I see I have 3 points'

        when: 'I post to /return with Trumper'
        then: 'trumper is returned'

        when: 'I go to /rent'
        then: 'I see only Clingon is rented'

    }
}
