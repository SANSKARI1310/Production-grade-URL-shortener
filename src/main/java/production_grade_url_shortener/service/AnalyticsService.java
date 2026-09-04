package production_grade_url_shortener.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import production_grade_url_shortener.repository.ClickEventRepository;
import production_grade_url_shortener.util.IdGenerator;
import production_grade_url_shortener.entity.ClickEvent;
import production_grade_url_shortener.event.UrlClickEvent;

@Service

public class AnalyticsService {
    
    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);
    private final IdGenerator idGenerator;
    private final ClickEventRepository clickEventRepository;

    public AnalyticsService(IdGenerator idGenerator , ClickEventRepository clickEventRepository)
    {
        this.idGenerator=idGenerator;
        this.clickEventRepository = clickEventRepository;

    }

    @Transactional
    public void handleUrlClick(UrlClickEvent event)
    {
        if(clickEventRepository.existsByEventId(event.eventId()))
        {
            log.debug("Event with id {} already exists" , event.eventId());
            return;
        }
        ClickEvent click = new ClickEvent(
        idGenerator.generateId(), 
        event.eventId(),
        event.shortcode(),
        event.originalUrl(),
        event.ipAddress(),
        event.userAgent(),
        event.referer(),
        event.timestamp()
        );
        clickEventRepository.save(click);
        log.debug("Persisted click event from kafka for the code {}" , event.shortcode());
        log.info("Kafka consumer tracked -- Code: {} , Ip: {} , TimeStamp: {}" , event.shortcode() , event.ipAddress() , event.timestamp());
    }

}
