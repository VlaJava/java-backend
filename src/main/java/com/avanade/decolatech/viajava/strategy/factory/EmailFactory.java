package com.avanade.decolatech.viajava.strategy.factory;

import com.avanade.decolatech.viajava.strategy.EmailStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class EmailFactory {

    private final Map<String, EmailStrategy> strategies = new HashMap<>();

    public EmailFactory(Set<EmailStrategy> strategies) {
        strategies.forEach(strategy -> this.strategies.put(strategy.getEmailType().name(), strategy));
    }

    public EmailStrategy getStrategy(String emailType) {
        EmailStrategy strategy = strategies.get(emailType);

        if(strategy == null) {
            throw new IllegalArgumentException(
                    String.format("[%s getStrategy] - Email type not found",
                            EmailFactory.class.getName()));
        }
        return strategy;
    }
}
