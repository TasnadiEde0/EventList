package edu.bbte.idde.teim2310.spring.dataaccess.indb;

import edu.bbte.idde.teim2310.spring.dataaccess.EventDao;
import edu.bbte.idde.teim2310.spring.exception.DataRetrievalException;
import edu.bbte.idde.teim2310.spring.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Repository
@Profile("indb")
public class InDbEventDao implements EventDao {

    @Autowired
    private DataSource connectionPool;

    private void setValuesInPreparedStatement(Event event, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, event.getName());
        preparedStatement.setString(2, event.getLocation());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(event.getStartDate()));
        preparedStatement.setTimestamp(4, Timestamp.valueOf(event.getEndDate()));
        preparedStatement.setInt(5, event.getReminderTime());
    }

    @Override
    public Event save(Event event) throws DataRetrievalException {

        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO Events(Name, Location, StartDate, EndDate, reminderTime) "
                        + "VALUES (?, ?, ?, ?, ?)")) {

            setValuesInPreparedStatement(event, preparedStatement);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.toString());
            throw new DataRetrievalException(e);
        }

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Events WHERE Name = ? AND "
                    + "Location = ? AND StartDate = CONVERT(DATETIME, CONVERT(DATETIME2, ?, 127)) AND "
                    + "EndDate = CONVERT(DATETIME, CONVERT(DATETIME2, ?, 127)) AND reminderTime = ?")) {

            setValuesInPreparedStatement(event, preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                event.setId((long) resultSet.getInt(1));
            }

        } catch (SQLException e) {
            log.error(e.toString());
            throw new DataRetrievalException(e);
        }

        return event;
    }

    private void setValuesInEvent(ResultSet resultSet, Event event) throws SQLException {
        event.setId(resultSet.getLong("ID"));
        event.setName(resultSet.getString("Name").trim());
        event.setLocation(resultSet.getString("Location").trim());
        event.setStartDate(resultSet.getTimestamp("StartDate").toLocalDateTime());
        event.setEndDate(resultSet.getTimestamp("EndDate").toLocalDateTime());
        event.setReminderTime(resultSet.getInt("reminderTime"));

    }

    @Override
    public Collection<Event> findAll() throws DataRetrievalException {

        Collection<Event> events = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Events");
            while (resultSet.next()) {
                Event event = new Event();

                setValuesInEvent(resultSet, event);

                events.add(event);
            }

        } catch (SQLException e) {
            log.error(e.toString());
            throw new DataRetrievalException(e);
        }

        return events;
    }

    @Override
    public Optional<Event> findById(Long id) throws DataRetrievalException {
        Event event = null;

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Events WHERE ID = ?")) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                event = new Event();

                setValuesInEvent(resultSet, event);

            }

        } catch (SQLException e) {
            log.error(e.toString());
            throw new DataRetrievalException(e);
        }

        return Optional.ofNullable(event);
    }

    @Override
    public void update(Event event) throws DataRetrievalException {
        if (findById(event.getId()).isEmpty()) {
            throw new NoSuchElementException();
        }

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Events SET Name = ?, "
                    + "Location = ?, StartDate = ?, EndDate = ?, reminderTime = ? WHERE ID = ?")) {

            preparedStatement.setLong(6, event.getId());

            setValuesInPreparedStatement(event, preparedStatement);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.toString());
            throw new DataRetrievalException(e);
        }
    }

    @Override
    public void delete(Event event) throws DataRetrievalException {
        if (event == null || findById(event.getId()).isEmpty()) {
            throw new NoSuchElementException();
        }

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Events WHERE ID = ?")) {

            preparedStatement.setLong(1, event.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.toString());
            throw new DataRetrievalException(e);
        }

    }

    @Override
    public Collection<Event> filterByReminder(Integer limit) throws DataRetrievalException {

        Collection<Event> events = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM Events WHERE reminderTime <= ?")) {

            preparedStatement.setInt(1, limit);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Event event = new Event();

                setValuesInEvent(resultSet, event);

                events.add(event);
            }

        } catch (SQLException e) {
            log.error(e.toString());
            throw new DataRetrievalException(e);
        }

        return events;
    }

}
