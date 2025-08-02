package com.avanade.decolatech.viajava.service.dashboard;

import com.avanade.decolatech.viajava.domain.dtos.response.view.GeneralMetricsView;
import com.avanade.decolatech.viajava.domain.dtos.response.view.MonthlyRevenueView;
import com.avanade.decolatech.viajava.domain.dtos.response.view.PendingBookingClientsView;
import com.avanade.decolatech.viajava.domain.dtos.response.view.PopularDestinationView;
import com.avanade.decolatech.viajava.domain.repository.BookingRepository;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PackageRepository packageRepository;

    public DashboardService(UserRepository userRepository, BookingRepository bookingRepository, PackageRepository packageRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.packageRepository = packageRepository;
    }

    @Transactional(readOnly = true)
    public GeneralMetricsView getGeneralMetrics() {
        return this.userRepository.getGeneralMetrics();
    }

    @Transactional(readOnly = true)
    public List<MonthlyRevenueView> getMonthlyRevenue() {
        return this.bookingRepository.findMonthlyRevenue();
    }

    @Transactional(readOnly = true)
    public List<PopularDestinationView> getPopularDestinations() {
        return this.packageRepository.findPopularDestinations();
    }

    @Transactional(readOnly = true)
    public List<PendingBookingClientsView> getPendingBookingClients() {
        return this.bookingRepository.findPendingBookingClients();
    }
}
