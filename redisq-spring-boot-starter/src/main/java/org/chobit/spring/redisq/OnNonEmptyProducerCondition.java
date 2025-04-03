package org.chobit.spring.redisq;

import org.chobit.spring.redisq.beetle.config.ProduceConfig;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.List;

/**
 * 用于检查配置中的producer列表是否为空
 *
 * @author robin
 * @since 2025/4/3 22:39
 */
public class OnNonEmptyProducerCondition extends SpringBootCondition {

	private final Bindable<List<ProduceConfig>> configList = Bindable.listOf(ProduceConfig.class);


	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {

		final String propertyName = "redisq.producer";
		BindResult<List<ProduceConfig>> property = Binder.get(context.getEnvironment()).bind(propertyName, configList);
		if (property.isBound() && !property.get().isEmpty()) {
			return ConditionOutcome.match();
		}
		return ConditionOutcome.noMatch(propertyName + " not exists");
	}
}
