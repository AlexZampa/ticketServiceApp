import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.annotation.Observed
import io.micrometer.observation.aop.ObservedAspect
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class ObserveConfiguration {

    @Bean
    fun observedAspect(observationRegistry: ObservationRegistry?): ObservedAspect {
        return ObservedAspect(observationRegistry!!)
    }
}