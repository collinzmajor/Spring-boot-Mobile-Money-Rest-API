package com.collinz.mobilemoney.config;

import com.collinz.mobilemoney.models.Charge;
import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.models.TransferLimit;
import com.collinz.mobilemoney.models.User;
import com.collinz.mobilemoney.models.enums.ChargeType;
import com.collinz.mobilemoney.models.enums.FeeType;
import com.collinz.mobilemoney.models.enums.Status;
import com.collinz.mobilemoney.models.enums.Timeframe;
import com.collinz.mobilemoney.models.keys.TransferLimitId;
import com.collinz.mobilemoney.repositories.ChargeRepository;
import com.collinz.mobilemoney.repositories.CurrencyRepository;
import com.collinz.mobilemoney.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class AppConfig {
    private final UserRepository userRepository;

    @Autowired
    public AppConfig(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Bean
    public UserDetailsService userDetailsService() throws UsernameNotFoundException{
        return username -> userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(CurrencyRepository currencyRepository, UserRepository userRepository){
        return args -> {
            Currency currency = new Currency(null, "USD", "$", "United States Dollar", LocalDateTime.now(), Status.ACTIVE);
            currency.setCharges(
                    List.of(
                            new Charge(null, "Mthuli tax", currency, new BigDecimal("10"), null, ChargeType.TRANSACTION, FeeType.DYNAMIC, new BigDecimal("2"), Status.ACTIVE, LocalDateTime.now()),
                            new Charge(null, "Service charge", currency, new BigDecimal("0"), null, ChargeType.TRANSACTION, FeeType.DYNAMIC, new BigDecimal("1"), Status.ACTIVE, LocalDateTime.now())
                    )
            );
            currency.setLimits(
                    List.of(
                            new TransferLimit(new TransferLimitId(null, Timeframe.TRANSACTION), new BigDecimal("100"),Status.ACTIVE, LocalDateTime.now(), currency),
                            new TransferLimit(new TransferLimitId(null, Timeframe.DAY), new BigDecimal("400"),Status.ACTIVE, LocalDateTime.now(), currency),
                            new TransferLimit(new TransferLimitId(null, Timeframe.WEEK), new BigDecimal("1000"),Status.ACTIVE, LocalDateTime.now(), currency),
                            new TransferLimit(new TransferLimitId(null, Timeframe.MONTH), new BigDecimal("5000"),Status.ACTIVE, LocalDateTime.now(), currency)
                    )
            );
            currencyRepository.save(currency);
        };
    }
}
