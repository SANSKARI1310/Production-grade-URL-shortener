package production_grade_url_shortener.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import org.springframework.scheduling.annotation.Async;
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

    @Async("AnalyticsExecutor")
    @EventListener
    @Transactional
    public void handleUrlClick(UrlClickEvent event)
    {
        try
        {
            ClickEvent click = new ClickEvent(
              idGenerator.generateId(),
              event.shortcode(),
              event.originalUrl(),
              event.ipAddress(),
              event.timestamp()
             );
             clickEventRepository.save(click);
             log.debug("Persisted click event for the code {}" , event.shortcode());
        }
        catch(Exception e)
        {
            
        }
        log.info("Background task tracked -- Code: {} , Ip: {} , TimeStamp: {}" , event.shortcode() , event.ipAddress() , event.timestamp());

    }

}
