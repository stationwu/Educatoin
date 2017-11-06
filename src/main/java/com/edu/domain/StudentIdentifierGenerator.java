package com.edu.domain;

import com.edu.utils.GoodNumberGenerator;
import org.hibernate.MappingException;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class StudentIdentifierGenerator
        implements IdentifierGenerator, Configurable {

    public static final String SEQUENCE_PREFIX = "sequence_prefix";
    private static final String STUDENT_SEQUENCE_NAME = "student_seq";

    private String sequencePrefix;

    private GoodNumberGenerator numberGenerator = new GoodNumberGenerator();

    private static final Logger logger = LoggerFactory.getLogger(StudentIdentifierGenerator.class);

    @Override
    public void configure(
            Type type, Properties params, ServiceRegistry serviceRegistry)
            throws MappingException {
        final JdbcEnvironment jdbcEnvironment =
                serviceRegistry.getService(JdbcEnvironment.class);

        final ConfigurationService configurationService =
                serviceRegistry.getService(ConfigurationService.class);
        String globalEntityIdentifierPrefix =
                configurationService.getSetting( "entity.identifier.prefix", String.class, "S");

        sequencePrefix = ConfigurationHelper.getString(
                SEQUENCE_PREFIX,
                params,
                globalEntityIdentifierPrefix);
    }

    @Override
    public Serializable generate(SessionImplementor session, Object obj) {
        if (obj instanceof Identifiable) {
            Identifiable identifiable = (Identifiable) obj;
            Serializable id = identifiable.getId();
            if (id != null) {
                return id;
            }
        }

        String generatedId = null;

        Connection connection = session.connection();

        try {
            PreparedStatement ps1 = connection.prepareStatement("SELECT tbl.next_val FROM sequence tbl WHERE tbl.seq_name=? FOR UPDATE");
            ps1.setString(1, STUDENT_SEQUENCE_NAME);

            ResultSet rs = ps1.executeQuery();

            if(rs.next())
            {
                Long seqValue = rs.getLong(1);
                generatedId = sequencePrefix + String.format("%08d%s", 0 , seqValue);

                logger.debug("Generated Id: " + generatedId);

                Long nextValue = generateNextValue(seqValue);

                PreparedStatement ps2 = connection.prepareStatement("UPDATE sequence SET next_val=? WHERE next_val=? AND seq_name=?");
                ps2.setLong(1, nextValue);
                ps2.setLong(2, seqValue);
                ps2.setString(3, STUDENT_SEQUENCE_NAME);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return generatedId;
    }

    private Long generateNextValue(Long currentValue) {
        return numberGenerator.next(currentValue, 1);
    }
}